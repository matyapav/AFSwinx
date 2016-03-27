package cz.cvut.fel.matyapav.afandroid.components.types;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import com.tomscz.afrest.commons.SupportedComponents;
import com.tomscz.afswinx.rest.connection.AFSwinxConnection;
import com.tomscz.afswinx.rest.connection.AFSwinxConnectionPack;
import com.tomscz.afswinx.rest.rebuild.BaseRestBuilder;
import com.tomscz.afswinx.rest.rebuild.RestBuilderFactory;
import com.tomscz.afswinx.rest.rebuild.holder.AFDataHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cz.cvut.fel.matyapav.afandroid.builders.widgets.AbstractWidgetBuilder;
import cz.cvut.fel.matyapav.afandroid.builders.widgets.WidgetBuilderFactory;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.rest.RequestTask;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Created by Pavel on 26.12.2015.
 */
public class AFForm extends AFComponent {

    public AFForm() {
    }

    public AFForm(Activity activity, AFSwinxConnectionPack connectionPack, Skin skin) {
        super(activity, connectionPack, skin);
    }

    @Override
    public void insertData(String dataResponse, StringBuilder road){
        try {
            JSONObject jsonObject = new JSONObject(dataResponse);
            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext()){
                String key = keys.next();
                if(jsonObject.get(key) instanceof JSONObject){
                    String roadBackup = road.toString();
                    road.append(key);
                    road.append(".");
                    insertData(jsonObject.get(key).toString(), road); //parse class types
                    road = new StringBuilder(roadBackup.toString());
                }else {
                    //System.err.println("ROAD+KEY" + (road + key));
                    AFField field = getFieldById(road + key);
                    //System.err.println("FIELD" + field);
                    if (field != null) {
                        setFieldValue(field, jsonObject.get(key));
                    }
                }

            }
        } catch (JSONException e) {
            System.err.println("CANNOT PARSE DATA");
            e.printStackTrace();
        }
    }

    private void setFieldValue(AFField field, Object val){
        WidgetBuilderFactory.getInstance().getFieldBuilder(field.getFieldInfo(), getSkin()).setData(field, val);
    }

    @Override
    public AFDataHolder reserialize() {
        AFDataHolder dataHolder = new AFDataHolder();
        for (AFField field : getFields()) {
            AbstractWidgetBuilder fieldBuilder =
                    WidgetBuilderFactory.getInstance().getFieldBuilder(field.getFieldInfo(), getSkin());
            Object data = fieldBuilder.getData(field);
            String propertyName = field.getId();
            // Based on dot notation determine road. Road is used to add object to its right place
            String[] roadTrace = propertyName.split("\\.");
            if (roadTrace.length > 1) {
                AFDataHolder startPoint = dataHolder;
                for (int i = 0; i < roadTrace.length; i++) {
                    String roadPoint = roadTrace[i];
                    // If road end then add this property as inner propety
                    if (i + 1 == roadTrace.length) {
                        startPoint.addPropertyAndValue(roadPoint, (String) data);
                    } else {
                        // Otherwise it will be inner class so add if doesn't exist continue.
                        AFDataHolder roadHolder = startPoint.getInnerClassByKey(roadPoint);
                        if (roadHolder == null) {
                            roadHolder = new AFDataHolder();
                            roadHolder.setClassName(roadPoint);
                            startPoint.addInnerClass(roadHolder);
                        }
                        // Set start point on current possition in tree
                        startPoint = roadHolder;
                    }
                }
            } else {
                dataHolder.addPropertyAndValue(propertyName, data.toString());
            }
        }
        return dataHolder;
    }

    @Override
    public SupportedComponents getComponentType() {
        return SupportedComponents.FORM;
    }

    @Override
    public boolean validateData(){
        boolean allValidationsFine = true;
        for(AFField field : getFields()){
            if(!field.validate()){
                allValidationsFine = false;
            }
        }
        return allValidationsFine;
    }

    /*TROCHU POUPRAVENA MARTINOVA METODA*/
    public boolean sendData() throws Exception{
        if(validateData()) {
            if (getConnectionPack().getSendConnection() == null) {
                throw new IllegalStateException(
                        "The post connection was not specify. Check your XML configuration or Connection which was used to build this form");
            }
            Object data = generateSendData();
            if (data == null) {
                return false;
            }
            System.err.println("SEND CONNECTION " + Utils.getConnectionEndPoint(getConnectionPack().getSendConnection()));
            RequestTask sendTask = new RequestTask(getConnectionPack().getSendConnection().getHttpMethod(), getConnectionPack().getSendConnection().getContentType(),
                    getConnectionPack().getSendConnection().getSecurity(), data, Utils.getConnectionEndPoint(getConnectionPack().getSendConnection()));
            Object response = sendTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            if (response instanceof Exception) {
                throw (Exception) response;
            }
            return true;
        }
        return false;
    }

    /*MARTINOVA METODA*/
    private Object generateSendData() {
        // before building data and sending, validate actual data
        boolean isValid = validateData();
        if (!isValid) {
            return null;
        }
        AFSwinxConnection sendConnection = getConnectionPack().getSendConnection();
        // Generate send connection based on which will be retrieve data. The send connection is
        // used to generate data in this case it will be generated JSON
        if (sendConnection == null) {
            sendConnection = new AFSwinxConnection("", 0, "");
        }
        BaseRestBuilder dataBuilder = RestBuilderFactory.getInstance().getBuilder(sendConnection);
        Object data = dataBuilder.reselialize(reserialize());
        return data;
    }

    public void hideErrors(){
        for(AFField field : getFields()){
            field.getErrorView().setVisibility(View.GONE);
        }
    }

    public void resetData() {
        for (AFField field: getFields()) {
            AbstractWidgetBuilder builder = WidgetBuilderFactory.getInstance().getFieldBuilder(field.getFieldInfo(), getSkin());
            builder.setData(field, field.getActualData());
        }
    }

    public void clearData() {
        for (AFField field: getFields()) {
            field.setActualData(null);
        }
        resetData();
    }

    public Object getDataFromFieldWithId(String id){
        AFField field = getFieldById(id);
        if(field != null){
            return WidgetBuilderFactory.getInstance().getFieldBuilder(field.getFieldInfo(), getSkin()).getData(field);
        }
        return null;
    }

    public void setDataToFieldWithId(String id, Object data){
        AFField field = getFieldById(id);
        WidgetBuilderFactory.getInstance().getFieldBuilder(field.getFieldInfo(), getSkin()).setData(field, data);
    }
}
