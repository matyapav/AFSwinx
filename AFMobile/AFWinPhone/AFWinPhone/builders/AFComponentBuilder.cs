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
    /// <summary>
    /// This class defines common parts of all component builders. Specific component builders must extend
    /// </summary>
    /// <typeparam name="T">Concrete type of builder</typeparam>
    public abstract class AFComponentBuilder<T>
    {
        private AFSwinxConnectionPack connectionPack;
        private Skin skin;
        private String connectionKey;
        private String componentKeyName;
        private String pathToConnectionResource;
        private Dictionary<String, String> connectionParameters;

        /// <summary>
        /// Initializes builder for component. This method should be used if getting definition of component
        /// from server does not need additional parameters like user credentials.
        /// </summary>
        /// <param name="componentKeyName">user defined name of component</param>
        /// <param name="pathToConnectionResource">path to XML file with defined connections</param>
        /// <param name="connectionKey">used to get specific connection definition from connection resource file.</param>
        /// <returns>concrete builder</returns>
        public AFComponentBuilder<T> initBuilder(String componentKeyName, String pathToConnectionResource, String connectionKey)
        {
            this.componentKeyName = componentKeyName;
            this.pathToConnectionResource = pathToConnectionResource;
            this.connectionKey = connectionKey;
            if (AfWindowsPhone.getInstance().getDefaultSkin() == null)
            {
                this.skin = new DefaultSkin();
            }
            else
            {
                this.skin = AfWindowsPhone.getInstance().getDefaultSkin();
            }
            return this;
        }

        /// <summary>
        /// Initializes builder for component. This method should be used if getting definition of component
        /// from server needs additional parameters like user credentials
        /// </summary>
        /// <param name="componentKeyName">user defined name of component</param>
        /// <param name="pathToConnectionResource">path to XML file with defined connections</param>
        /// <param name="connectionKey">used to get specific connection definition from connection resource file.</param>
        /// <param name="connectionParameters">additional parameters for connection like user credentials</param>
        /// <returns>concrete builder</returns>
        public AFComponentBuilder<T> initBuilder(String componentKeyName, String pathToConnectionResource,
                         String connectionKey, Dictionary<String, String> connectionParameters)
        {
            this.componentKeyName = componentKeyName;
            this.pathToConnectionResource = pathToConnectionResource;
            this.connectionKey = connectionKey;
            this.connectionParameters = connectionParameters;
            if (AfWindowsPhone.getInstance().getDefaultSkin() == null)
            {
                this.skin = new DefaultSkin();
            }
            else
            {
                this.skin = AfWindowsPhone.getInstance().getDefaultSkin();
            }
            return this;
        }

        /// <summary>
        /// Initializes connections for component. Creates and sets connections from specified file to builder.
        /// Must be called before creating any part of component and after initialization of builder.
        /// Exception throwed if not connection was specified, that means initialization of builder was not called.
        /// </summary>
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

        /// <summary>
        /// Prepares whole component especially its graphical representation which can be inserted in UI.
        /// </summary>
        /// <param name="classDef">information about component</param>
        /// <param name="component">which component is being prepared</param>
        /// <param name="numberOfInnerClasses">number of inner classes which actual class definition has</param>
        /// <param name="parsingInnerClass">determines if preparing UI from inner class</param>
        /// <param name="road">Passed to field buidlers. If not empty, field belongs to some inner class. This fact must be set into fields id.</param>
        protected void prepareComponent(AFClassInfo classDef, AFComponent component, int numberOfInnerClasses, bool parsingInnerClass, StringBuilder road)
        {
            if (parsingInnerClass)
            {
                numberOfInnerClasses = 0;
            }
            if (classDef != null)
            {
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

        /// <summary>
        /// Builds and wraps component UI from class definition and sets it to component.
        /// Exception thrown if there is some error during parsing component definition or building component view
        /// </summary>
        /// <param name="component">component its UI is being built wrapped and set.</param>
        protected void buildComponent(AFComponent component)
        {

            StackPanel componentView = new StackPanel();
            //componentView.setLayoutParams(getSkin().getTopLayoutParams());
            JSONParser parser = new JSONDefinitionParser();

            var modelTask = Task.Run(component.getModelResponse);
            modelTask.Wait();
            String modelResponse = modelTask.Result;

            AFClassInfo classDef = parser.parse(modelResponse, false);
            component.setComponentInfo(classDef);
            prepareComponent(classDef, component, 0, false, new StringBuilder());
            FrameworkElement view = buildComponentView(component);
            componentView.Children.Add(view);
            component.setView(componentView);
        }


        /// <summary>
        /// Builder specific method which creates final version of component which is presented to user. Should be used by user to create component.
        /// Exception thrown if there is an error during crating process. User must handle this exception.
        /// </summary>
        /// <returns>completely created component</returns>
        public abstract AFComponent createComponent();

        /// <summary>
        /// Builder specific method used only for building graphical representation of component.
        /// </summary>
        /// <param name="component">components its UI is being build.</param>
        /// <returns>UI of component</returns>
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

        public AFSwinxConnectionPack getConnectionPack()
        {
            return connectionPack;
        }
    }
}
