package cz.cvut.fel.matyapav.afandroid.components.types;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.ViewGroup;

import com.tomscz.afrest.layout.definitions.LayouDefinitions;
import com.tomscz.afrest.layout.definitions.LayoutOrientation;
import com.tomscz.afrest.rest.dto.AFClassInfo;
import com.tomscz.afswinx.rest.connection.AFSwinxConnection;
import com.tomscz.afswinx.rest.connection.AFSwinxConnectionPack;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.rest.RequestMaker;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Common implementation of {@link AbstractComponent}. Implements common methods for each component.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public abstract class AFComponent implements AbstractComponent{

    private Activity activity;
    private AFClassInfo componentInfo;
    private ViewGroup view;
    private List<AFField> fields;
    private AFSwinxConnectionPack connectionPack;
    private Skin skin;

    public AFComponent(){
    }

    public AFComponent(Activity activity, AFSwinxConnectionPack connectionPack, Skin skin) {
        this.activity = activity;
        this.connectionPack = connectionPack;
        this.skin = skin;
    }

    /**
     * Insert data method which should be used by end users.
     *
     * @param dataObject data to be inserted
     */
    public void insertData(Object dataObject){
        insertData(dataObject.toString(), new StringBuilder());
    }

    /**
     * Adds a field in component.
     *
     * @param field field to be added
     */
    public void addField(AFField field){
        if(fields == null){
            fields = new ArrayList<AFField>();
        }
        field.setParent(this);
        fields.add(field);
    }

    /**
     * Gets the field with defined identifier.
     *
     * @param id identifier of field
     * @return corresponding field
     */
    public AFField getFieldById(String id){
        for (AFField field: getFields()) {
            if(field.getId().equals(id)){
                return field;
            }
        }
        //not found
        return null;
    }

    /**
     * Gets count of field which are visible to user.
     *
     * @return number of visible fields
     */
    public int getVisibleFieldsCount(){
        int res = 0;
        for (AFField field: getFields()) {
            if(field.getFieldInfo().getVisible()){
                res++;
            }
        }
        return res;
    }

    @Override
    public String getModelResponse() throws Exception{
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

    @Override
    public String getDataResponse() throws Exception{
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

    public Skin getSkin() {
        return skin;
    }

    public List<AFField> getFields() {
        return fields;
    }

    public Activity getActivity() {
        return activity;
    }

    public AFSwinxConnectionPack getConnectionPack() {
        return connectionPack;
    }

    public ViewGroup getView() {
        return view;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setConnectionPack(AFSwinxConnectionPack connectionPack) {
        this.connectionPack = connectionPack;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public void setFields(List<AFField> fields) {
        this.fields = fields;
    }

    public void setView(ViewGroup view) {
        this.view = view;
    }

    public AFClassInfo getComponentInfo() {
        return componentInfo;
    }

    public void setComponentInfo(AFClassInfo componentInfo) {
        this.componentInfo = componentInfo;
    }
}
