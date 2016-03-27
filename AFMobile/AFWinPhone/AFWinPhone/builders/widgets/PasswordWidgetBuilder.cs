using AFWinPhone.components.parts;
using AFWinPhone.builders.skins;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media;
using Windows.UI;

namespace AFWinPhone.builders.widgets
{
    class PasswordWidgetBuilder : BasicBuilder
    {
        public PasswordWidgetBuilder(Skin skin, AFFieldInfo properties) : base(skin, properties)
        {
        }

        public override FrameworkElement buildFieldView()
        {
            PasswordBox password = new PasswordBox();
            password.Foreground = new SolidColorBrush(getSkin().getFieldColor());
            password.FontFamily = getSkin().getFieldFont();
            password.FontSize = getSkin().getFieldFontSize();
            if (getProperties().isReadOnly())
            {
                password.IsEnabled = false;
                password.Foreground = new SolidColorBrush(Colors.LightGray);
            }
            return password;
        }

        public override object getData(AFField field)
        {
            return ((PasswordBox)field.getFieldView()).Password;
        }

        public override void setData(AFField field, object value)
        {
            if (value != null)
            {
                ((PasswordBox)field.getFieldView()).Password = value.ToString();
                field.setActualData(value);
            }
            else {
                ((PasswordBox)field.getFieldView()).Password = "";
                field.setActualData("");
            }
        }
    }
}
