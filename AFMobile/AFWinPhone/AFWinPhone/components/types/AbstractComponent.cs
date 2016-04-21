using AFWinPhone.enums;
using AFWinPhone.rest.holder;
using System;
using System.Text;
using System.Threading.Tasks;

namespace AFWinPhone.components.types
{
    /// <summary>
    /// Interface for components. Defines methods which should be implemented by components. Some of methods 
    /// like validate or send data won't be used by read only components (like AFList)
    /// </summary>
    interface AbstractComponent
    {
        /// <summary>
        /// Inserts data to component.
        /// </summary>
        /// <param name="dataResponse">response with data in some format</param>
        /// <param name="road">used for inserting data into fields created from inner classes</param>
        void insertData(String dataResponse, StringBuilder road);

        /// <summary>
        /// Gets the type of component
        /// </summary>
        /// <returns>component type</returns>
        SupportedComponents getComponentType();


        /// <summary>
        /// Validates data in component.
        /// </summary>
        /// <returns>false if any validation failed.</returns>
        bool validateData();

        /// <summary>
        /// Converts data to representation suitable for server needs.
        /// </summary>
        /// <returns>reserialized data</returns>
        AFDataHolder reserialize();

        /// <summary>
        /// Gets metamodel response from server.
        /// Exception thrown if there was connection problem.
        /// </summary>
        /// <returns>metamodel string from server</returns>
        Task<String> getModelResponse();

        /// <summary>
        /// Gets data response from server.
        /// Exception thrown if there was connection problem.
        /// </summary>
        /// <returns>data string from server</returns>
        Task<String> getDataResponse();

        /// <summary>
        /// Generates data, which should be sent to server.
        /// </summary>
        /// <returns>generated data</returns>
        Object generateSendData();

        /// <summary>
        /// Sends data back to server.
        /// Exception thrown if there was problem in connection.
        /// </summary>
        /// <returns>return false if there was problem in generating data, true if data was successfully generated and sended to server.</returns>
        Task<Boolean> sendData();

    }
}
