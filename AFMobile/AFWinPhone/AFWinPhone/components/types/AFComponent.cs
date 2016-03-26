using AFWinPhone.components.parts;
using AFWinPhone.builders.skins;
using AFWinPhone.enums;
using AFWinPhone.rest.connection;
using AFWinPhone.rest.holder;
using System;
using System.Collections.Generic;
using System.Text;
using Windows.UI.Xaml;

namespace AFWinPhone.components.types
{
    public abstract class AFComponent : AbstractComponent
    {
        private String name;
        private FrameworkElement view;
        private LayoutDefinitions layoutDefinitions;
        private LayoutOrientation layoutOrientation;
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

        public void addField(AFField field)
        {
            if (fields == null)
            {
                fields = new List<AFField>();
            }
            field.setParent(this);
            fields.Add(field);
        }

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

        //GETTERS
        public String getName()
        {
            return name;
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

        public LayoutOrientation getLayoutOrientation()
        {
            return this.layoutOrientation;
        }

        public LayoutDefinitions getLayoutDefinitions()
        {
            return this.layoutDefinitions;
        }

        //SETTERS
        public void setName(String name)
        {
            this.name = name;
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

        public void setLayoutDefinitions(LayoutDefinitions layoutDefinitions)
        {
            this.layoutDefinitions = layoutDefinitions;
        }

        public void setLayoutOrientation(LayoutOrientation layoutOrientation)
        {
            this.layoutOrientation = layoutOrientation;
        }

        public void insertData(String dataResponse)
        {
            insertData(dataResponse, new StringBuilder());
        }

        public abstract void insertData(string dataResponse, StringBuilder road);
        public abstract SupportedComponents getComponentType();
        public abstract bool validateData();
        public abstract AFDataHolder reserialize();
    }
}
