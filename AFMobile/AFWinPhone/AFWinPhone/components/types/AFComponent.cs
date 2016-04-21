using AFWinPhone.components.parts;
using AFWinPhone.builders.skins;
using AFWinPhone.enums;
using AFWinPhone.rest.connection;
using AFWinPhone.rest.holder;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using Windows.UI.Xaml;
using AFWinPhone.rest;
using AFWinPhone.utils;

namespace AFWinPhone.components.types
{

    /// <summary>
    /// Common implementation of AbstractComponent. Implements common methods for each component.
    /// </summary>
    public abstract class AFComponent : AbstractComponent
    {
        private AFClassInfo componentInfo;
        private FrameworkElement view;
        private List<AFField> fields;
        private AFSwinxConnectionPack connectionPack;
        private Skin skin;

        public AFComponent()
        {
        }

        public AFComponent(AFSwinxConnectionPack connectionPack, Skin skin)
        {
            this.connectionPack = connectionPack;
            this.skin = skin;
        }

        /// <summary>
        /// Insert data method which should be used by end users.
        /// </summary>
        /// <param name="dataObject">data to be inserted</param>
        public void insertData(Object dataObject)
        {
            insertData(dataObject.ToString(), new StringBuilder());
        }

        /// <summary>
        /// Adds a field in component.
        /// </summary>
        /// <param name="field">field to be added</param>
        public void addField(AFField field)
        {
            if (fields == null)
            {
                fields = new List<AFField>();
            }
            field.setParent(this);
            fields.Add(field);
        }

        /// <summary>
        /// Gets the field with defined identifier.
        /// </summary>
        /// <param name="id">identifier of field</param>
        /// <returns>corresponding field</returns>
        public AFField getFieldById(String id)
        {
            foreach (AFField field in getFields())
            {
                if (field.getId().Equals(id))
                {
                    return field;
                }
            }
            //not found
            return null;
        }

        /// <summary>
        /// Gets count of field which are visible to user.
        /// </summary>
        /// <returns>number of visible fields</returns>
        public int getVisibleFieldsCount()
        {
            int res = 0;
            foreach (AFField field in getFields())
            {
                if (field.getFieldInfo().isVisible())
                {
                    res++;
                }
            }
            return res;
        }


        public AFClassInfo getComponentInfo()
        {
            return componentInfo;
        }

        public Skin getSkin()
        {
            return skin;
        }

        public List<AFField> getFields()
        {
            return fields;
        }

        public AFSwinxConnectionPack getConnectionPack()
        {
            return this.connectionPack;
        }

        public FrameworkElement getView()
        {
            return this.view;
        }

        public void setConnectionPack(AFSwinxConnectionPack connectionPack)
        {
            this.connectionPack = connectionPack;
        }

        public void setSkin(Skin skin)
        {
            this.skin = skin;
        }

        public void setFields(List<AFField> fields)
        {
            this.fields = fields;
        }

        public void setView(FrameworkElement view)
        {
            this.view = view;
        }

        public void setComponentInfo(AFClassInfo classInfo)
        {
            this.componentInfo = classInfo;
        }

        public async Task<String> getModelResponse()
        {
            AFSwinxConnection modelConnection = connectionPack.getMetamodelConnection();
            if (modelConnection != null)
            {
                RequestMaker maker = new RequestMaker(modelConnection.getHttpMethod(), modelConnection.getContentType(),
                        modelConnection.getSecurity(), null, Utils.GetConnectionEndPoint(modelConnection));

                String modelResponse = await maker.doRequest();
                return modelResponse;
            }
            else {
                throw new Exception("No model connection available. Did you call initializeConnections() before?");
            }
        }

        public async Task<String> getDataResponse()
        {
            AFSwinxConnection dataConnection = connectionPack.getDataConnection();
            if (dataConnection != null)
            {
                RequestMaker getData = new RequestMaker(dataConnection.getHttpMethod(), dataConnection.getContentType(),
                        dataConnection.getSecurity(), null, Utils.GetConnectionEndPoint(dataConnection));
                String response = await getData.doRequest();
                return response;
            }
            return null;
        }

        public abstract object generateSendData();
        public abstract Task<Boolean> sendData();
        public abstract void insertData(string dataResponse, StringBuilder road);
        public abstract SupportedComponents getComponentType();
        public abstract bool validateData();
        public abstract AFDataHolder reserialize();
    }
}
