package cz.cvut.fel.matyapav.afandroid.components.types;

import cz.cvut.fel.matyapav.afandroid.enums.SupportedComponents;

/**
 * Created by Pavel on 29.02.2016.
 */
public class AFComponentFactory {

    private static AFComponentFactory instance = null;

    public static synchronized AFComponentFactory getInstance() {
        if(instance == null){
            instance = new AFComponentFactory();
        }
        return instance;
    }

    public AFComponent getComponentByType(SupportedComponents type){
        if(type.equals(SupportedComponents.FORM)) {
            return new AFForm();
        }else if(type.equals(SupportedComponents.TABLE)){
            return new AFTable();
        }else if(type.equals(SupportedComponents.LIST)){
            return new AFList();
        }else{
            ///type not supported;
            return null;
        }

    }
}
