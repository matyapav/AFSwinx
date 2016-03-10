﻿using AFWindowsPhone.builders.components.parts;
using AFWindowsPhone.builders.skins;
using AFWindowsPhone.builders.widgets;
using AFWindowsPhone.enums;
using AFWindowsPhone.rest;
using AFWindowsPhone.utils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Data.Json;

namespace AFWindowsPhone.builders.components.types
{
    class AFForm : AFComponent
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

        public override void insertData(string dataResponse, StringBuilder road)
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
                        insertData(jsonObject[key].ToString(), road); //parse class types
                        road = new StringBuilder(roadBackup.ToString());
                    }else {
                        //System.err.println("ROAD+KEY" + (road + key));
                        AFField field = getFieldById(road + key);
                        //System.err.println("FIELD" + field);
                        if (field != null)
                        {
                            setFieldValue(field, jsonObject[key]);
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

        private void setFieldValue(AFField field, Object val)
        {
            WidgetBuilderFactory.getInstance().getFieldBuilder(field.getFieldInfo(), getSkin()).setData(field, val);
        }


        public override AFDataHolder reserialize()
        {
            AFDataHolder dataHolder = new AFDataHolder();
            foreach (AFField field in getFields())
            {
                AbstractWidgetBuilder fieldBuilder =
                        WidgetBuilderFactory.getInstance().getFieldBuilder(field.getFieldInfo(), getSkin());
                Object data = fieldBuilder.getData(field);
                String propertyName = field.getId();
                // Based on dot notation determine road. Road is used to add object to its right place
                String[] roadTrace = propertyName.Split(new String[] { "\\." }, StringSplitOptions.None);
                if (roadTrace.Length > 1)
                {
                    AFDataHolder startPoint = dataHolder;
                    for (int i = 0; i < roadTrace.Length; i++)
                    {
                        String roadPoint = roadTrace[i];
                        // If road end then add this property as inner propety
                        if (i + 1 == roadTrace.Length)
                        {
                            startPoint.addPropertyAndValue(roadPoint, (String)data);
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

       
        public void sendData()
        {
            if(getConnectionPack().getSendConnection() == null) 
               {
                    throw new Exception(
                            "The post connection was not specify. Check your XML configuration or Connection which was used to build this form");
                }
                Object data = generateSendData();
            if(data == null) {
                    return;
                }
                Debug.WriteLine("SEND CONNECTION "+ Utils.getConnectionEndPoint(getConnectionPack().getSendConnection()));
                RequestTask sendTask = new RequestTask(getActivity(),getConnectionPack().getSendConnection().getHttpMethod(), getConnectionPack().getSendConnection().getContentType(),
                    getConnectionPack().getSendConnection().getSecurity(), data, Utils.getConnectionEndPoint(getConnectionPack().getSendConnection()));
            Object response = sendTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            if(response is Exception){
                throw (Exception) response;
            }
        }

        private Object generateSendData()
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

        public void resetData()
        {
            foreach (AFField field in getFields())
            {
                AbstractWidgetBuilder builder = WidgetBuilderFactory.getInstance().getFieldBuilder(field.getFieldInfo(), getSkin());
                builder.setData(field, field.getActualData());
            }
        }

        public void clearData()
        {
            foreach (AFField field in getFields())
            {
                field.setActualData(null);
            }
            resetData();
        }

        public Object getDataFromFieldWithId(String id)
        {
            AFField field = getFieldById(id);
            if (field != null)
            {
                return WidgetBuilderFactory.getInstance().getFieldBuilder(field.getFieldInfo(), getSkin()).getData(field);
            }
            return null;
        }

        public void setDataToFieldWithId(String id, Object data)
        {
            AFField field = getFieldById(id);
            WidgetBuilderFactory.getInstance().getFieldBuilder(field.getFieldInfo(), getSkin()).setData(field, data);
        }


    }
}
