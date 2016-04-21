package cz.cvut.fel.matyapav.afandroid;

import java.util.HashMap;

import cz.cvut.fel.matyapav.afandroid.builders.FormBuilder;
import cz.cvut.fel.matyapav.afandroid.builders.ListBuilder;
import cz.cvut.fel.matyapav.afandroid.builders.TableBuilder;
import cz.cvut.fel.matyapav.afandroid.builders.skins.DefaultSkin;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.components.types.AFComponent;

/**
 * Facade for framework usage. Creating or getting components should be done using this class.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class AFAndroid {

    private static AFAndroid instance = null;
    private HashMap<String, AFComponent> createdComponents;
    private Skin defaulSkin;

    public AFAndroid() {
        createdComponents = new HashMap<>();
    }

    /**
     * Gets the isntance of AFAndroid class
     * @return instance of AFAndroid class
     */
    public static synchronized AFAndroid getInstance(){
        if(instance == null){
            instance = new AFAndroid();
        }
        return instance;
    }

    /**
     * Gets created component by name which user defines when component is created.
     *
     * @param name name used for getting the component
     * @return corresponding component
     */
    public AFComponent getCreateComponentByName(String name){
        return createdComponents.get(name);
    }

    /**
     * Adds pair name-component to map of created components.
     * @param name name of component
     * @param component component to add
     */
    public void addCreatedComponent(String name, AFComponent component){
        createdComponents.put(name,component);
    }

    /**
     * Removes component from map of created components.
     *
     * @param name component name
     */
    public void removeCreatedComponent(String name){
        createdComponents.remove(name);
    }

    /**
     * Removes all created components.
     *
     */
    public void removeAll(){
        createdComponents.clear();
    }

    public Skin getDefaulSkin() {
        return defaulSkin;
    }

    /**
     * Sets default skin used within component create process.
     * @param defaulSkin skin to be used
     */
    public void setDefaulSkin(Skin defaulSkin) {
        this.defaulSkin = defaulSkin;
    }

    /**
     * Gets the builder for creating form component.
     * @return form builder
     */
    public FormBuilder getFormBuilder(){
        return new FormBuilder();
    }

    /**
     * Gets the builder for creating table component.
     * @return table builder
     */
    public TableBuilder getTableBuilder() {
        return new TableBuilder();
    }

    /**
     * Gets the builder for creating list component.
     * @return list builder
     */
    public ListBuilder getListBuilder(){
        return new ListBuilder();
    }
}
