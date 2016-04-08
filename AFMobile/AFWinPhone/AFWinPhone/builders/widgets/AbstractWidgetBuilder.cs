using AFWinPhone.components.parts;
using System;
using Windows.UI.Xaml;

namespace AFWinPhone.builders.widgets
{
    public interface AbstractWidgetBuilder
    {

        FrameworkElement buildFieldView();

        void setData(Object value);

        Object getData();
    }
}
