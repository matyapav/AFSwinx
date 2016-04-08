package cz.cvut.fel.matyapav.afandroid.builders;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import com.tomscz.afrest.commons.SupportedComponents;
import com.tomscz.afrest.rest.dto.AFClassInfo;
import com.tomscz.afrest.rest.dto.AFFieldInfo;
import com.tomscz.afswinx.rest.connection.AFSwinxConnection;
import com.tomscz.afswinx.rest.connection.AFSwinxConnectionPack;
import com.tomscz.afswinx.rest.connection.ConnectionParser;

import org.json.JSONException;

import java.io.InputStream;
import java.util.HashMap;

import cz.cvut.fel.matyapav.afandroid.builders.widgets.FieldBuilder;
import cz.cvut.fel.matyapav.afandroid.components.types.AFComponent;
import cz.cvut.fel.matyapav.afandroid.components.types.AFComponentFactory;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.builders.skins.DefaultSkin;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.parsers.JSONDefinitionParser;
import cz.cvut.fel.matyapav.afandroid.parsers.JSONParser;
import cz.cvut.fel.matyapav.afandroid.rest.RequestMaker;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Created by Pavel on 19.02.2016.
 */
public abstract class AFComponentBuilder<T> {

    private Activity activity;
    private AFSwinxConnectionPack connectionPack;
    private Skin skin;
    private String connectionKey;
    private String componentKeyName;
    private InputStream connectionResource;
    private HashMap<String, String> connectionParameters;


    public T initBuilder(Activity activity, String componentKeyName, InputStream connectionResource, String connectionKey){
        this.activity = activity;
        this.componentKeyName = componentKeyName;
        this.connectionResource = connectionResource;
        this.connectionKey = connectionKey;
        this.skin = new DefaultSkin(activity);
        return (T) this;
    }

    public T initBuilder(Activity activity, String componentKeyName, InputStream connectionResource,
                         String connectionKey, HashMap<String, String> connectionParameters) {
        this.activity = activity;
        this.componentKeyName = componentKeyName;
        this.connectionResource = connectionResource;
        this.connectionKey = connectionKey;
        this.connectionParameters = connectionParameters;
        this.skin = new DefaultSkin(activity);
        return (T) this;
    }

    protected void initializeConnections() throws Exception {
        if (connectionPack == null && connectionKey != null && connectionResource != null) {
            ConnectionParser connectionParser =
                    new ConnectionParser(connectionKey, connectionParameters);
            AFSwinxConnectionPack connections =
                    connectionParser.parseDocument(com.tomscz.afswinx.common.Utils
                            .buildDocumentFromFile(connectionResource));
            connectionPack = connections;
        } else {
            // Model connection is important if it could be found then throw exception
            throw new Exception(
                    "There is error during building AFForm. Connection was not specified. Did you used initBuilder method before build?");
        }
    }

    protected void prepareComponent(AFClassInfo classDef, AFComponent component, int numberOfInnerClasses, boolean parsingInnerClass, StringBuilder road){
        if(parsingInnerClass){
            numberOfInnerClasses = 0;
        }
        if(classDef != null) {
            //fieldsView = (TableLayout) buildLayout(classDef, activity);
            FieldBuilder builder = new FieldBuilder();
            for (AFFieldInfo field : classDef.getFieldInfo()) {
                if(field.getClassType()){
                    String roadBackup = road.toString();
                    road.append(classDef.getInnerClasses().get(numberOfInnerClasses).getName());
                    road.append(".");
                    prepareComponent(classDef.getInnerClasses().get(numberOfInnerClasses), component, numberOfInnerClasses++, true, road);
                    road = new StringBuilder(roadBackup);
                }else {
                    AFField affield = builder.prepareField(field, road, getActivity(), skin);
                    if (affield != null) {
                        component.addField(affield);
                    }
                }
            }
        }
        System.err.println("NUMBER OF ELEMENTS IN COMPONENT " + component.getFields().size());
    }

    protected AFComponent buildComponent(String modelResponse, SupportedComponents type) throws JSONException {
        AFComponent component = AFComponentFactory.getInstance().getComponentByType(type);
        component.setActivity(getActivity());
        component.setConnectionPack(connectionPack);
        component.setSkin(skin);

        LinearLayout componentView = new LinearLayout(getActivity());
        componentView.setLayoutParams(getSkin().getTopLayoutParams());
        JSONDefinitionParser parser = new JSONDefinitionParser();
        AFClassInfo classDef = parser.parse(modelResponse, false);

        component.setComponentInfo(classDef);
        prepareComponent(classDef, component, 0, false, new StringBuilder());
        View view = buildComponentView(component);
        componentView.addView(view);
        component.setView(componentView);
        return component;
    }

    protected String getModelResponse() throws Exception{
        AFSwinxConnection modelConnection = connectionPack.getMetamodelConnection();
        if(modelConnection != null) {
            RequestMaker task = new RequestMaker(modelConnection.getHttpMethod(), modelConnection.getContentType(),
                    modelConnection.getSecurity(), null, Utils.getConnectionEndPoint(modelConnection));

            Object modelResponse = task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get(); //make it synchronous to wait for response
            if (modelResponse instanceof Exception) {
                throw (Exception) modelResponse;
            }
            return (String) modelResponse;
        }else{
            throw new Exception("No model connection available. Did you call initializeConnections() before?");
        }
    }

    protected String getDataResponse() throws Exception{
        AFSwinxConnection dataConnection = connectionPack.getDataConnection();
        if(dataConnection != null) {
            RequestMaker getData = new RequestMaker(dataConnection.getHttpMethod(), dataConnection.getContentType(),
                    dataConnection.getSecurity(), null, Utils.getConnectionEndPoint(dataConnection));
            Object response = getData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            if (response instanceof Exception) {
                throw (Exception) response;
            }
            return (String) response;
        }
        return null;
    }

    public abstract AFComponent createComponent() throws Exception;

    protected abstract View buildComponentView(AFComponent component);

    public Activity getActivity() {
        return activity;
    }

    public String getComponentKeyName() {
        return componentKeyName;
    }

    public T setSkin(Skin skin){
        this.skin = skin;
        return (T) this;
    }

    public Skin getSkin() {
        return skin;
    }


}
