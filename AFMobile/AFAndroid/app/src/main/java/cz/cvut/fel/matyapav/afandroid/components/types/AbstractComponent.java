package cz.cvut.fel.matyapav.afandroid.components.types;

import com.tomscz.afrest.commons.SupportedComponents;
import com.tomscz.afswinx.rest.rebuild.holder.AFDataHolder;

/**
 * Interface for components. Defines methods which should be implemented by components. Some of methods
 * like validate or send data won't be used by read only components (like {@link AFList})
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public interface AbstractComponent {

    /**
     * Inserts data to component.
     *
     * @param dataResponse response with data in some format
     * @param road used for inserting data into fields created from inner classes
     */
    void insertData(String dataResponse, StringBuilder road);

    /**
     * Gets the type of component
     * @return component type
     */
    SupportedComponents getComponentType();

    /**
     * Gets metamodel response from server.
     *
     * @return metamodel string from server
     * @throws Exception thrown if there was connection problem.
     */
    String getModelResponse() throws Exception;

    /**
     * Gets data response from server.
     *
     * @return data string from server
     * @throws Exception thrown if there was connection problem.
     */
    String getDataResponse() throws Exception;

    /**
     * Validates data in component.
     *
     * @return false if any validation failed.
     */
    boolean validateData();

    /**
     * Sends data back to server.
     *
     * @return return false if there was problem in generating data, true if data was successfully generated and sended to server.
     * @throws Exception thrown if there was problem in connection.
     */
    boolean sendData() throws Exception;

    /**
     * Generates data, which should be sent to server.
     *
     * @return generated data
     */
    Object generateSendData();

    /**
     * Converts data to representation suitable for server needs.
     *
     * @return reserialized data
     */
    AFDataHolder reserialize();

}
