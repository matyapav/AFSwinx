package cz.cvut.fel.matyapav.afandroid.components.types;

import com.tomscz.afrest.commons.SupportedComponents;
import com.tomscz.afswinx.rest.rebuild.holder.AFDataHolder;

/**
 * Created by Pavel on 29.02.2016.
 */
public interface AbstractComponent {

    void insertData(String dataResponse, StringBuilder road);

    SupportedComponents getComponentType();

    boolean validateData();

    AFDataHolder reserialize();

}
