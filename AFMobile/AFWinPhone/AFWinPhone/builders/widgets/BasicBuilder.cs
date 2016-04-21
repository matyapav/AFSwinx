using AFWinPhone.components.parts;
using AFWinPhone.builders.skins;
using Windows.UI.Xaml;

namespace AFWinPhone.builders.widgets
{
    /// <summary>
    /// Basic implementation of AbstractWidgetBuilder. Defines that widget builders needs skin
    /// and information about field. Every concrete implementation of widget builder should extends this class.
    /// </summary>
    abstract class BasicBuilder : AbstractWidgetBuilder
    {
        private Skin skin;
        private AFField field;

        public BasicBuilder(Skin skin, AFField field)
        {
            this.skin = skin;
            this.field = field;
        }

        public Skin getSkin()
        {
            return skin;
        }

        public AFField getField()
        {
            return field;
        }

        public abstract FrameworkElement buildFieldView();
        public abstract void setData(object value);
        public abstract object getData();
    }
}
