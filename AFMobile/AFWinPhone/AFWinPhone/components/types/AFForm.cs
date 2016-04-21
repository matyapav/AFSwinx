using AFWinPhone.components.parts;
using AFWinPhone.builders.skins;
using AFWinPhone.builders.widgets;
using AFWinPhone.enums;
using AFWinPhone.rest;
using AFWinPhone.rest.connection;
using AFWinPhone.rest.holder;
using AFWinPhone.utils;
using System;
using System.Diagnostics;
using System.Text;
using System.Threading.Tasks;
using Windows.Data.Json;
using Windows.UI.Xaml;

namespace AFWinPhone.components.types
{
    /// <summary>
    /// Representing a form component.
    /// </summary>
    public class AFForm : AFComponent
    {
        public AFForm()
        {
        }

        public AFForm(AFSwinxConnectionPack connectionPack, Skin skin) : base(connectionPack, skin)
        {
        }

        public override SupportedComponents getComponentType()
        {
            return SupportedComponents.FORM;
        }

        public override void insertData(String dataResponse, StringBuilder road)
        {
            try
            {
                JsonObject jsonObject = JsonObject.Parse(dataResponse);
                var keys = jsonObject.Keys;
                foreach (String key in keys)
                {
                    if (jsonObject[key].ValueType == JsonValueType.Object){
                        String roadBackup = road.ToString();
                        road.Append(key);
                        road.Append(".");
                        insertData(jsonObject[key].Stringify(), road); //parse class types
                        road = new StringBuilder(roadBackup);
                    }else {
                        //System.err.println("ROAD+KEY" + (road + key));
                        AFField field = getFieldById(road + key);
                        //System.err.println("FIELD" + field);
                        if (field != null)
                        {
                            field.getWidgetBuilder().setData(Utils.TryToGetValueFromJson(jsonObject[key]));
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Debug.WriteLine("CANNOT PARSE DATA");
                Debug.WriteLine(e.StackTrace);
            }

        }

        public override AFDataHolder reserialize()
        {
            AFDataHolder dataHolder = new AFDataHolder();
            foreach (AFField field in getFields())
            {
                Object data = field.getWidgetBuilder().getData();
                
                //little workaroud to fit in server //TODO if ever this thing will be released delete this!!
                if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.NUMBERDOUBLEFIELD))
                {
                    data = ((String) data).Replace(',', '.');
                }

                String propertyName = field.getId();
                // Based on dot notation determine road. Road is used to add object to its right place
                String[] roadTrace = propertyName.Split(new [] {"."}, StringSplitOptions.None);
                if (roadTrace.Length > 1)
                {
                    AFDataHolder startPoint = dataHolder;
                    for (int i = 0; i < roadTrace.Length; i++)
                    {
                        String roadPoint = roadTrace[i];
                        // If road end then add this property as inner propety
                        if (i + 1 == roadTrace.Length)
                        {
                            startPoint.addPropertyAndValue(roadPoint, (String) data);
                        }
                        else {
                            // Otherwise it will be inner class so add if doesn't exist continue.
                            AFDataHolder roadHolder = startPoint.getInnerClassByKey(roadPoint);
                            if (roadHolder == null)
                            {
                                roadHolder = new AFDataHolder();
                                roadHolder.setClassName(roadPoint);
                                startPoint.addInnerClass(roadHolder);
                            }
                            // Set start point on current possition in tree
                            startPoint = roadHolder;
                        }
                    }
                }
                else {
                    dataHolder.addPropertyAndValue(propertyName, data.ToString());
                }
            }
            return dataHolder;

        }

        public override bool validateData()
        {
            bool allValidationsFine = true;
            foreach (AFField field in getFields())
            {
                if (!field.validate())
                {
                    allValidationsFine = false;
                }
            }
            return allValidationsFine;

        }

        /// <summary>
        /// Hides validation errors.
        /// </summary>
        public void hideErrors()
        {
            foreach (var field in getFields())
            {
                field.getErrorView().Visibility = Visibility.Collapsed;
            }
        }

        public override async Task<Boolean> sendData()
        {
            if (getConnectionPack().getSendConnection() == null)
            {
                throw new Exception(
                    "The post connection was not specify. Check your XML configuration or Connection which was used to build this form");
            }
            Object data = generateSendData();
            if (data == null)
            {
                return false;
            }
            Debug.WriteLine("SEND CONNECTION " +
                            Utils.GetConnectionEndPoint(getConnectionPack().getSendConnection()));
            RequestMaker sendMaker = new RequestMaker(getConnectionPack().getSendConnection().getHttpMethod(),
                getConnectionPack().getSendConnection().getContentType(),
                getConnectionPack().getSendConnection().getSecurity(), data,
                Utils.GetConnectionEndPoint(getConnectionPack().getSendConnection()));
            await sendMaker.doRequest();
            return true;
        }

        public override Object generateSendData()
        {
            // before building data and sending, validate actual data
            bool isValid = validateData();
            if (!isValid)
            {
                return null;
            }
            AFSwinxConnection sendConnection = getConnectionPack().getSendConnection();
            // Generate send connection based on which will be retrieve data. The send connection is
            // used to generate data in this case it will be generated JSON
            if (sendConnection == null)
            {
                sendConnection = new AFSwinxConnection("", 0, "");
            }
            BaseRestBuilder dataBuilder = RestBuilderFactory.getInstance().getBuilder(sendConnection);
            Object data = dataBuilder.reselialize(reserialize());
            return data;
        }

        /// <summary>
        /// Resets data in form. If there were no data set, it just clears the form.
        /// </summary>
        public void resetData()
        {
            foreach (AFField field in getFields())
            {
                field.getWidgetBuilder().setData(field.getActualData());
            }
        }

        /// <summary>
        /// Clears the form - sets all fields to empty or default value.
        /// </summary>
        public void clearData()
        {
            foreach (AFField field in getFields())
            {
                field.setActualData(null);
            }
            resetData();
        }

        /// <summary>
        /// Gets data from field given by specified identifier.
        /// </summary>
        /// <param name="id">identifier of field</param>
        /// <returns> data from field</returns>
        public Object getDataFromFieldWithId(String id)
        {
            AFField field = getFieldById(id);
            if (field != null)
            {
                return field.getWidgetBuilder().getData();
            }
            return null;
        }

        /// <summary>
        /// Sets data to field gived by specified identifier.
        /// </summary>
        /// <param name="id">identifier of field</param>
        /// <param name="data">data to be set</param>
        public void setDataToFieldWithId(String id, Object data)
        {
            AFField field = getFieldById(id);
            field.getWidgetBuilder().setData(data);
        }


    }
}
