using AFWinPhone.components.parts;
using AFWinPhone.builders.skins;
using System;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media;

namespace AFWinPhone.builders.widgets
{
    /// <summary>
    /// This class is responsible for building checkbox element.
    /// </summary>
    class CheckboxWidgetBuilder : BasicBuilder
    {
        public CheckboxWidgetBuilder(Skin skin, AFField field) : base(skin, field)
        {
        }

        public override FrameworkElement buildFieldView()
        {
            CheckBox checkBox = new CheckBox();
            checkBox.FontSize = getSkin().getFieldFontSize();
            checkBox.Foreground = new SolidColorBrush(getSkin().getFieldColor());
            if (getField().getFieldInfo().isReadOnly())
            {
                checkBox.IsEnabled = false;
            }
            return checkBox;
        }

        public override void setData(Object value)
        {
            CheckBox box = (CheckBox)getField().getFieldView();
            box.IsChecked = Convert.ToBoolean(value.ToString());
            getField().setActualData(value);
        }

        public override Object getData()
        {
            CheckBox box = (CheckBox)getField().getFieldView();
            return box.IsChecked.ToString().ToLower();
        }
    }
}
