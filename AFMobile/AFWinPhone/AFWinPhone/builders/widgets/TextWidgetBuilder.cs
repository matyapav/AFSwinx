using AFWinPhone.components.parts;
using AFWinPhone.builders.skins;
using AFWinPhone.enums;
using System;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;

namespace AFWinPhone.builders.widgets
{
    class TextWidgetBuilder : BasicBuilder
    {
        public TextWidgetBuilder(Skin skin, AFFieldInfo properties) : base(skin, properties)
        {
        }

        public override FrameworkElement buildFieldView()
        {
            TextBox text = new TextBox();
            text.Foreground = new SolidColorBrush(getSkin().getFieldColor());
            text.FontFamily = getSkin().getFieldFont();
            text.FontSize = getSkin().getFieldFontSize();
            addInputType(text, getProperties().getWidgetType());
            if (getProperties().isReadOnly())
            {
                text.IsEnabled = false;
                text.Foreground = new SolidColorBrush(Colors.LightGray);
            }
            return text;
        }

        public override void setData(AFField field, Object value)
        {
            if (value != null)
            {
                ((TextBox)field.getFieldView()).Text = value.ToString();
                field.setActualData(value);
            }
            else {
                ((TextBox)field.getFieldView()).Text = "";
                field.setActualData("");
            }

        }

        public override Object getData(AFField field)
        {
            return ((TextBox)field.getFieldView()).Text;
        }

        private void addInputType(TextBox field, SupportedWidgets widgetType)
        {
            InputScope scope = new InputScope();
            InputScopeName name = new InputScopeName();
            //textfield or password
            if (widgetType.Equals(SupportedWidgets.TEXTFIELD))
            {
                name.NameValue = InputScopeNameValue.Default;
            }
            else if (widgetType.Equals(SupportedWidgets.NUMBERFIELD) || widgetType.Equals(SupportedWidgets.NUMBERDOUBLEFIELD))
            {
                name.NameValue = InputScopeNameValue.Number;
            }
            scope.Names.Add(name);
            field.InputScope = scope;
        }
    }
}
