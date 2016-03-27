using AFWinPhone.components.parts;
using AFWinPhone.builders.skins;
using Windows.UI.Xaml;

namespace AFWinPhone.builders.widgets
{
    abstract class BasicBuilder : AbstractWidgetBuilder
    {
        private Skin skin;
        private AFFieldInfo properties;

        public BasicBuilder(Skin skin, AFFieldInfo properties)
        {
            this.skin = skin;
            this.properties = properties;
        }

        public Skin getSkin()
        {
            return skin;
        }

        public AFFieldInfo getProperties()
        {
            return properties;
        }

        public abstract FrameworkElement buildFieldView();
        public abstract void setData(AFField field, object value);
        public abstract object getData(AFField field);
    }
}
