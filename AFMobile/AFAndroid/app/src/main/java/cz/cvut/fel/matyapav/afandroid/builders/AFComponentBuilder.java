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
 * This class defines common parts of all component builders. Specific component builders must extend
 * this class.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public abstract class AFComponentBuilder<T> {

    private Activity activity;
    private AFSwinxConnectionPack connectionPack;
    private Skin skin;
    private String connectionKey;
    private String componentKeyName;
    private InputStream connectionResource;
    private HashMap<String, String> connectionParameters;


    /**
     * Initializes builder for component. This method should be used if getting definition of component
     * from server does not need additional parameters like user credentials.
     *
     * @param activity needed to dynamically create UI in runtime.
     * @param componentKeyName user defined name of component
     * @param connectionResource input stream with XML file, which defines connections
     * @param connectionKey used to get specific connection definition from connectionResource file.
     * @return concrete builder
     */
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

    /**
     * Initializes builder for component. This method should be used if getting definition of component
     * from server needs additional parameters like user credentials
     *
     * @param activity needed to dynamically create UI in runtime.
     * @param componentKeyName user defined name of component
     * @param connectionResource input stream with XML file, which defines connections
     * @param connectionKey used to get specific connection definition from connectionResource file.
     * @param connectionParameters additional parameters for connection like user credentials
     * @return
     */
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

    /**
     * Initializes connections for component. Creates and sets connections from specified file to builder.
     * Must be called before creating any part of component and after initialization of builder.
     *
     * @throws Exception throwed if not connection was specified, that means initialization of builder was not called.
     */
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
                    "There is error during building component. Connection was not specified. Did you used initBuilder method before build?");
        }
    }

    /**
     * Prepares whole component especially its graphical representation which can be inserted in UI.
     *
     * @param classDef information about component
     * @param component which component is being prepared
     * @param numberOfInnerClasses number of inner classes which actual class definition has
     * @param parsingInnerClass determines if preparing UI from inner class
     * @param road passed to field buidlers. If not empty, field belongs to some inner class. This fact must be set into fields id.
     */
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

    /**
     * Builds and wraps component UI from class definition and sets it to component.
     *
     * @param component component its UI is being built wrapped and set.
     * @throws Exception thrown if there is some error during parsing component definition or building component view
     */
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

    /**
     * Builder specific method which creates final version of component which is presented to user.
     * Should be used by user to create component.
     *
     * @return completely created component
     * @throws Exception thrown if there is an error during crating process. User must handle this exception.
     */
    public abstract AFComponent createComponent() throws Exception;

    /**
     * Builder specific method used only for building graphical representation of component.
     *
     * @param component components its UI is being build.
     * @return UI of component
     */
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
