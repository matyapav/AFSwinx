package cz.cvut.fel.matyapav.afandroid;

import java.util.HashMap;

import cz.cvut.fel.matyapav.afandroid.builders.FormBuilder;
import cz.cvut.fel.matyapav.afandroid.builders.ListBuilder;
import cz.cvut.fel.matyapav.afandroid.builders.TableBuilder;
import cz.cvut.fel.matyapav.afandroid.builders.skins.DefaultSkin;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.components.types.AFComponent;

/**
 * Created by Pavel on 13.02.2016.
 */
public class AFAndroid {

    private static AFAndroid instance = null;
    private HashMap<String, AFComponent> createdComponents;
    private Skin defaulSkin;

    public AFAndroid() {
        createdComponents = new HashMap<>();
    }

    public static synchronized AFAndroid getInstance(){
        if(instance == null){
            instance = new AFAndroid();
        }
        return instance;
    }

    public AFComponent getCreateComponentByName(String name){
        return createdComponents.get(name);
    }

    public void addCreatedComponent(String name, AFComponent component){
        createdComponents.put(name,component);
    }

    public void removeCreatedCompoent(String name){
        createdComponents.remove(name);
    }

    public void removeAll(){
        createdComponents.clear();
    }

    public Skin getDefaulSkin() {
        return defaulSkin;
    }

    public void setDefaulSkin(Skin defaulSkin) {
        this.defaulSkin = defaulSkin;
    }

    public FormBuilder getFormBuilder(){
        return new FormBuilder();
    }

    public TableBuilder getTableBuilder() {
        return new TableBuilder();
    }

    public ListBuilder getListBuilder(){
        return new ListBuilder();
    }
}
