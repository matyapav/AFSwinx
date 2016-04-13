package cz.cvut.fel.matyapav.afandroid.components.types;

import com.tomscz.afrest.commons.SupportedComponents;
import com.tomscz.afswinx.rest.rebuild.holder.AFDataHolder;

/**
 * Created by Pavel on 29.02.2016.
 */
public interface AbstractComponent {

    void insertData(String dataResponse, StringBuilder road);

    SupportedComponents getComponentType();

    String getModelResponse() throws Exception;

    String getDataResponse() throws Exception;

    boolean validateData();

    boolean sendData() throws Exception;

    Object generateSendData();

    AFDataHolder reserialize();

}
