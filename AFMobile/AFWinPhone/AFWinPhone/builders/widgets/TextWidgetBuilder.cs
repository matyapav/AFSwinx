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
        public TextWidgetBuilder(Skin skin, AFField field) : base(skin, field)
        {
        }

        public override FrameworkElement buildFieldView()
        {
            TextBox text = new TextBox();
            text.Foreground = new SolidColorBrush(getSkin().getFieldColor());
            text.FontFamily = getSkin().getFieldFont();
            text.FontSize = getSkin().getFieldFontSize();
            addInputType(text, getField().getFieldInfo().getWidgetType());
            if (getField().getFieldInfo().isReadOnly())
            {
                text.IsEnabled = false;
                text.Foreground = new SolidColorBrush(Colors.LightGray);
            }
            return text;
        }

        public override void setData(Object value)
        {
            if (value != null)
            {
                ((TextBox)getField().getFieldView()).Text = value.ToString();
                getField().setActualData(value);
            }
            else {
                ((TextBox)getField().getFieldView()).Text = "";
                getField().setActualData("");
            }

        }

        public override Object getData()
        {
            return ((TextBox)getField().getFieldView()).Text;
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
