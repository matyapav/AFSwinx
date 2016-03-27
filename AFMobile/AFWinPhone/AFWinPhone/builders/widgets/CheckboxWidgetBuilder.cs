using AFWinPhone.components.parts;
using AFWinPhone.builders.skins;
using System;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media;

namespace AFWinPhone.builders.widgets
{
    class CheckboxWidgetBuilder : BasicBuilder
    {
        public CheckboxWidgetBuilder(Skin skin, AFFieldInfo properties) : base(skin, properties)
        {
        }

        public override FrameworkElement buildFieldView()
        {
            CheckBox checkBox = new CheckBox();
            checkBox.FontSize = getSkin().getFieldFontSize();
            checkBox.Foreground = new SolidColorBrush(getSkin().getFieldColor());
            //checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (getProperties().isReadOnly())
            {
                checkBox.IsEnabled = false;
            }
            return checkBox;
        }

        public override void setData(AFField field, Object value)
        {
            CheckBox box = (CheckBox)field.getFieldView();
            box.IsChecked = Convert.ToBoolean(value.ToString());
            field.setActualData(value);
        }

        public override Object getData(AFField field)
        {
            CheckBox box = (CheckBox)field.getFieldView();
            return box.IsChecked.ToString().ToLower();
        }
    }
}
