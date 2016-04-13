package cz.cvut.fel.matyapav.afandroid.builders;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.tomscz.afrest.rest.dto.AFClassInfo;
import com.tomscz.afrest.rest.dto.AFFieldInfo;
import com.tomscz.afswinx.rest.connection.AFSwinxConnectionPack;
import com.tomscz.afswinx.rest.connection.ConnectionParser;

import java.io.InputStream;
import java.util.HashMap;

import cz.cvut.fel.matyapav.afandroid.AFAndroid;
import cz.cvut.fel.matyapav.afandroid.builders.widgets.FieldBuilder;
import cz.cvut.fel.matyapav.afandroid.components.types.AFComponent;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.builders.skins.DefaultSkin;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.parsers.JSONDefinitionParser;

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
        if(AFAndroid.getInstance().getDefaulSkin() == null) {
            this.skin = new DefaultSkin(activity);
        }else{
            this.skin = AFAndroid.getInstance().getDefaulSkin();
        }
        return (T) this;
    }

    public T initBuilder(Activity activity, String componentKeyName, InputStream connectionResource,
                         String connectionKey, HashMap<String, String> connectionParameters) {
        this.activity = activity;
        this.componentKeyName = componentKeyName;
        this.connectionResource = connectionResource;
        this.connectionKey = connectionKey;
        this.connectionParameters = connectionParameters;
        if(AFAndroid.getInstance().getDefaulSkin() == null) {
            this.skin = new DefaultSkin(activity);
        }else{
            this.skin = AFAndroid.getInstance().getDefaulSkin();
        }
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

    protected void buildComponent(AFComponent component) throws Exception {
        LinearLayout componentView = new LinearLayout(getActivity());
        componentView.setLayoutParams(getSkin().getTopLayoutParams());
        JSONDefinitionParser parser = new JSONDefinitionParser();
        AFClassInfo classDef = parser.parse(component.getModelResponse(), false);

        component.setComponentInfo(classDef);
        prepareComponent(classDef, component, 0, false, new StringBuilder());
        View view = buildComponentView(component);
        componentView.addView(view);
        component.setView(componentView);
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

    public AFSwinxConnectionPack getConnectionPack() {
        return connectionPack;
    }
}
