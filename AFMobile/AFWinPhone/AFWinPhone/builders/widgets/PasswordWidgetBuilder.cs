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
        public PasswordWidgetBuilder(Skin skin, AFField field) : base(skin, field)
        {
        }

        public override FrameworkElement buildFieldView()
        {
            PasswordBox password = new PasswordBox();
            password.Foreground = new SolidColorBrush(getSkin().getFieldColor());
            password.FontFamily = getSkin().getFieldFont();
            password.FontSize = getSkin().getFieldFontSize();
            if (getField().getFieldInfo().isReadOnly())
            {
                password.IsEnabled = false;
                password.Foreground = new SolidColorBrush(Colors.LightGray);
            }
            return password;
        }

        public override object getData()
        {
            return ((PasswordBox)getField().getFieldView()).Password;
        }

        public override void setData(object value)
        {
            if (value != null)
            {
                ((PasswordBox)getField().getFieldView()).Password = value.ToString();
                getField().setActualData(value);
            }
            else {
                ((PasswordBox)getField().getFieldView()).Password = "";
                getField().setActualData("");
            }
        }
    }
}
