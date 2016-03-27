using AFWinPhone.components.parts;
using AFWinPhone.builders.skins;
using AFWinPhone.enums;
using AFWinPhone.utils;
using System.Diagnostics;

namespace AFWinPhone.builders.widgets
{
    class WidgetBuilderFactory
    {
        private static WidgetBuilderFactory instance = null;

        public static WidgetBuilderFactory getInstance()
        {
            if (instance == null)
            {
                instance = new WidgetBuilderFactory();
            }
            return instance;
        }


        public AbstractWidgetBuilder getFieldBuilder(AFFieldInfo properties, Skin skin)
        {
            if (Utils.IsFieldWritable(properties.getWidgetType()))
            {
                return new TextWidgetBuilder(skin, properties);
            }
            if (properties.getWidgetType().Equals(SupportedWidgets.CALENDAR))
            {
                return new DateWidgetBuilder(skin, properties);
            }
            if (properties.getWidgetType().Equals(SupportedWidgets.OPTION))
            {
                return new OptionWidgetBuilder(skin, properties);
            }
            if (properties.getWidgetType().Equals(SupportedWidgets.DROPDOWNMENU))
            {
                return new DropDownWidgetBuilder(skin, properties);
            }
            if (properties.getWidgetType().Equals(SupportedWidgets.CHECKBOX))
            {
                return new CheckboxWidgetBuilder(skin, properties);
            }
            if (properties.getWidgetType().Equals(SupportedWidgets.PASSWORD))
            {
                return new PasswordWidgetBuilder(skin, properties);
            }
            Debug.WriteLine("BUILDER FOR " + properties.getWidgetType() + " NOT FOUND");
            return null;
        }
    }
}
