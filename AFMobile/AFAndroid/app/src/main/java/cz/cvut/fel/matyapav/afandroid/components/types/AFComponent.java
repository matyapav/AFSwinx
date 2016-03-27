package cz.cvut.fel.matyapav.afandroid.components.types;

import android.app.Activity;
import android.view.ViewGroup;

import com.tomscz.afrest.layout.definitions.LayouDefinitions;
import com.tomscz.afrest.layout.definitions.LayoutOrientation;
import com.tomscz.afswinx.rest.connection.AFSwinxConnectionPack;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;

/**
 * Created by Pavel on 13.02.2016.
 */
public abstract class AFComponent implements AbstractComponent{

    private Activity activity;
    private String name;
    private ViewGroup view;
    private LayouDefinitions layoutDefinitions;
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

    //this one should be used by users
    public void insertData(Object dataObject){
        insertData(dataObject.toString(), new StringBuilder());
    }

    public void addField(AFField field){
        if(fields == null){
            fields = new ArrayList<AFField>();
        }
        field.setParent(this);
        fields.add(field);
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
            if(field.getFieldInfo().getVisible()){
                res++;
            }
        }
        return res;
    }

    //GETTERS
    public String getName() {
        return name;
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

    public LayoutOrientation getLayoutOrientation() {
        return layoutOrientation;
    }

    public LayouDefinitions getLayoutDefinitions() {
        return layoutDefinitions;
    }

    //SETTERS
    public void setName(String name) {
        this.name = name;
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

    public void setLayoutDefinitions(LayouDefinitions layoutDefinitions) {
        this.layoutDefinitions = layoutDefinitions;
    }

    public void setLayoutOrientation(LayoutOrientation layoutOrientation) {
        this.layoutOrientation = layoutOrientation;
    }


}
