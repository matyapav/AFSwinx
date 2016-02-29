package cz.cvut.fel.matyapav.afandroid.components.types;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;

import com.tomscz.afswinx.rest.connection.AFSwinxConnection;
import com.tomscz.afswinx.rest.connection.AFSwinxConnectionPack;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.components.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.enums.LayoutDefinitions;
import cz.cvut.fel.matyapav.afandroid.enums.LayoutOrientation;
import cz.cvut.fel.matyapav.afandroid.enums.SupportedComponents;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Created by Pavel on 13.02.2016.
 */
public abstract class AFComponent implements AbstractComponent{

    private Activity activity;
    private String name;
    private ViewGroup view;
    private LayoutDefinitions layoutDefinitions;
    private LayoutOrientation layoutOrientation;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ViewGroup getView() {
        return view;
    }

    public void setView(ViewGroup view) {
        this.view = view;
    }

    public LayoutDefinitions getLayoutDefinitions() {
        return layoutDefinitions;
    }

    public void setLayoutDefinitions(LayoutDefinitions layoutDefinitions) {
        this.layoutDefinitions = layoutDefinitions;
    }

    public LayoutOrientation getLayoutOrientation() {
        return layoutOrientation;
    }

    public void setLayoutOrientation(LayoutOrientation layoutOrientation) {
        this.layoutOrientation = layoutOrientation;
    }

    //this one should be used by users
    public void insertData(Object dataObject){
        insertData(dataObject.toString(), new StringBuilder());
    }


    public AFSwinxConnection getDataConnection() {
        return connectionPack.getDataConnection();
    }

    public AFSwinxConnection getModelConnection() {
        return connectionPack.getMetamodelConnection();
    }

    public AFSwinxConnection getSendConnection() {
        return connectionPack.getSendConnection();
    }

    public void addField(AFField field){
        if(fields == null){
            fields = new ArrayList<AFField>();
        }
        fields.add(field);
    }

    public List<AFField> getFields() {
        return fields;
    }

    public void setFields(List<AFField> fields) {
        this.fields = fields;
    }

    public Activity getActivity() {
        return activity;
    }

    public Skin getSkin() {
        return skin;
    }

    public AFField getFieldById(String id){
        for (AFField field: getFields()) {
            if(field.getId().equals(id)){
                return field;
            }
        }
        //not found
        return null;
    }

    public int getVisibleFieldsCount(){
        int res = 0;
        for (AFField field: getFields()) {
            if(field.getFieldInfo().isVisible()){
                res++;
            }
        }
        return res;
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
}