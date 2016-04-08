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


        public AbstractWidgetBuilder getFieldBuilder(AFField field, Skin skin)
        {
            if (Utils.IsFieldWritable(field.getFieldInfo().getWidgetType()))
            {
                return new TextWidgetBuilder(skin, field);
            }
            if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.CALENDAR))
            {
                return new DateWidgetBuilder(skin, field);
            }
            if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.OPTION))
            {
                return new OptionWidgetBuilder(skin, field);
            }
            if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.DROPDOWNMENU))
            {
                return new DropDownWidgetBuilder(skin, field);
            }
            if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.CHECKBOX))
            {
                return new CheckboxWidgetBuilder(skin, field);
            }
            if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.PASSWORD))
            {
                return new PasswordWidgetBuilder(skin, field);
            }
            Debug.WriteLine("BUILDER FOR " + field.getFieldInfo().getWidgetType() + " NOT FOUND");
            return null;
        }
    }
}
