using AFWinPhone.components;
using AFWinPhone.components.parts;
using AFWinPhone.components.types;
using AFWinPhone.builders.skins;
using AFWinPhone.enums;
using AFWinPhone.parsers;
using AFWinPhone.rest;
using AFWinPhone.rest.connection;
using AFWinPhone.utils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using System.Threading.Tasks;
using Windows.Data.Json;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using AFWinPhone.builders.widgets;

namespace AFWinPhone.builders
{
    public abstract class AFComponentBuilder<T>
    {
        private AFSwinxConnectionPack connectionPack;
        private Skin skin;
        private String connectionKey;
        private String componentKeyName;
        private String pathToConnectionResource;
        private Dictionary<String, String> connectionParameters;

        public AFComponentBuilder<T> initBuilder(String componentKeyName, String pathToConnectionResource, String connectionKey)
        {
            this.componentKeyName = componentKeyName;
            this.pathToConnectionResource = pathToConnectionResource;
            this.connectionKey = connectionKey;
            this.skin = new DefaultSkin();
            return this;
        }

        public AFComponentBuilder<T> initBuilder(String componentKeyName, String pathToConnectionResource,
                         String connectionKey, Dictionary<String, String> connectionParameters)
        {
            this.componentKeyName = componentKeyName;
            this.pathToConnectionResource = pathToConnectionResource;
            this.connectionKey = connectionKey;
            this.connectionParameters = connectionParameters;
            this.skin = new DefaultSkin();
            return this;
        }

        protected void initializeConnections()
        {
            if (connectionPack == null && connectionKey != null && pathToConnectionResource != null)
            {
                ConnectionParser connectionParser =
                        new ConnectionParser(connectionKey, connectionParameters);
                AFSwinxConnectionPack connections =
                        connectionParser.parseDocument(Utils
                                .BuildDocumentFromFile(pathToConnectionResource));
                connectionPack = connections;
            }
            else {
                // Model connection is important if it could be found then throw exception
                throw new Exception(
                        "There is error during building AFSwinxForm. Connection was not specified. Did you used initBuilder method before build?");
            }
        }

        protected void prepareComponent(AFClassInfo classDef, AFComponent component, int numberOfInnerClasses, bool parsingInnerClass, StringBuilder road)
        {
            if (parsingInnerClass)
            {
                numberOfInnerClasses = 0;
            }
            if (classDef != null)
            {
                if (!parsingInnerClass)
                { //set following properties only once at the beginning
                    component.setName(classDef.getClassName());
                    if (classDef.getLayout() != null)
                    {
                        component.setLayoutDefinitions(classDef.getLayout().getLayoutDefinition());
                        component.setLayoutOrientation(classDef.getLayout().getLayoutOrientation());
                    }
                }
                //fieldsView = (TableLayout) buildLayout(classDef, activity);
                FieldBuilder builder = new FieldBuilder();
                foreach (AFFieldInfo field in classDef.getFieldInfos())
                {
                    if (field.isInnerClass())
                    {
                        String roadBackup = road.ToString();
                        road.Append(classDef.getInnerClasses()[numberOfInnerClasses].getClassName());
                        road.Append(".");
                        prepareComponent(classDef.getInnerClasses()[numberOfInnerClasses], component, numberOfInnerClasses++, true, road);
                        road = new StringBuilder(roadBackup);
                    }
                    else {
                        AFField affield = builder.prepareField(field, road, skin);
                        if (affield != null)
                        {
                            component.addField(affield);
                        }
                    }
                }
            }
            Debug.WriteLine("NUMBER OF ELEMENTS IN COMPONENT " + component.getFields().Count);
        }

        protected AFComponent buildComponent(String modelResponse, SupportedComponents type)
        {
            AFComponent component = AFComponentFactory.getInstance().getComponentByType(type);

            component.setConnectionPack(connectionPack);
            component.setSkin(skin);

            StackPanel componentView = new StackPanel();
            //componentView.setLayoutParams(getSkin().getTopLayoutParams());
            JSONParser parser = new JSONDefinitionParser();
            AFClassInfo classDef = parser.parse(modelResponse, false);
            prepareComponent(classDef, component, 0, false, new StringBuilder());
            FrameworkElement view = buildComponentView(component);
            componentView.Children.Add(view);
            component.setView(componentView);
            return component;
        }

        protected async Task<String> getModelResponse()
        {
            AFSwinxConnection modelConnection = connectionPack.getMetamodelConnection();
            if (modelConnection != null)
            {
                RequestTask task = new RequestTask(modelConnection.getHttpMethod(), modelConnection.getContentType(),
                        modelConnection.getSecurity(), null, Utils.GetConnectionEndPoint(modelConnection));

                String modelResponse = await task.doRequest();
                return modelResponse;
            }
            else {
                throw new Exception("No model connection available. Did you call initializeConnections() before?");
            }
        }

        protected async Task<String> getDataResponse()
        {
            AFSwinxConnection dataConnection = connectionPack.getDataConnection();
            if (dataConnection != null)
            {
                RequestTask getData = new RequestTask(dataConnection.getHttpMethod(), dataConnection.getContentType(),
                        dataConnection.getSecurity(), null, Utils.GetConnectionEndPoint(dataConnection));
                String response = await getData.doRequest();
                return response;
            }
            return null;
        }

        public abstract AFComponent createComponent();

        protected abstract FrameworkElement buildComponentView(AFComponent component);

        public String getComponentKeyName()
        {
            return componentKeyName;
        }

        public void setComponentKeyName(String componentKeyName)
        {
            this.componentKeyName = componentKeyName;
        }

        public Skin getSkin()
        {
            return skin;
        }

        public AFComponentBuilder<T> setSkin(Skin skin)
        {
            this.skin = skin;
            return this;
        }

    }
}
