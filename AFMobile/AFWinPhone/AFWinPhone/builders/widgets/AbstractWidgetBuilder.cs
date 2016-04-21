using AFWinPhone.components.parts;
using System;
using Windows.UI.Xaml;

namespace AFWinPhone.builders.widgets
{
    /// <summary>
    /// Template for widget builders and it defines methods which every widget builder should have.
    /// </summary>
    public interface AbstractWidgetBuilder
    {

        /// <summary>
        /// This method is used for build graphical representation of widget - field's active element,
        /// that means without validation or label elements.
        /// </summary>
        /// <returns>graphical representation of field's active element</returns>
        FrameworkElement buildFieldView();

        /// <summary>
        /// Sets data to widget - field's active element
        /// </summary>
        /// <param name="value">data to set (usually String)</param>
        void setData(Object value);

        /// <summary>
        /// Gets data from widget - field's active element
        /// </summary>
        /// <returns>data from widget</returns>
        Object getData();
    }
}
