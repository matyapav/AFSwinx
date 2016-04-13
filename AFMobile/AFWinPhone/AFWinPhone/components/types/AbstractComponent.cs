using AFWinPhone.enums;
using AFWinPhone.rest.holder;
using System;
using System.Text;
using System.Threading.Tasks;

namespace AFWinPhone.components.types
{
    interface AbstractComponent
    {
        void insertData(String dataResponse, StringBuilder road);

        SupportedComponents getComponentType();

        bool validateData();

        AFDataHolder reserialize();

        Task<String> getModelResponse();

        Task<String> getDataResponse();

        Object generateSendData();

        Task<Boolean> sendData();

    }
}
