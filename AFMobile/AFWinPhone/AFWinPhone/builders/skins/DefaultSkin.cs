using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Media;

namespace AFWinPhone.builders.skins
{
    /// <summary>
    /// This class represents default implementation of {@link Skin} interface and will be used
    ///if user did not specify own skin.User's skins should extends this class because
    ///it gives ability to define only needed parts of skin.
    /// </summary>
    public class DefaultSkin : Skin
    {
        public virtual int getComponentMarginBottom()
        {
            return 20;
        }

        public virtual int getComponentMarginLeft()
        {
            return 0;
        }

        public virtual int getComponentMarginRight()
        {
            return 0;
        }

        public virtual int getComponentMarginTop()
        {
            return 0;
        }

        public virtual Color getFieldColor()
        {
            return Colors.Black;
        }

        public virtual FontFamily getFieldFont()
        {
            return new FontFamily("Segoe UI");
        }

        public virtual int getFieldFontSize()
        {
            return 18;
        }

        public virtual HorizontalAlignment getInputHorizontalAlignment()
        {
            return HorizontalAlignment.Stretch;
        }

        public virtual int getInputWidth()
        {
            return 200;
        }

        public virtual Color getLabelColor()
        {
            if (Application.Current.RequestedTheme.Equals(ApplicationTheme.Dark))
            {
                return Colors.White;
            }
            else
            {
                return Colors.Black;
            }
        }

        public virtual FontFamily getLabelFont()
        {
            return new FontFamily("Segoe UI");
        }

        public virtual int getLabelFontSize()
        {
            return 18;
        }

        public virtual int getLabelHeight()
        {
            return -1; //see label verticall alignement
        }

        public virtual HorizontalAlignment getLabelHorizontalAlignment()
        {
            return HorizontalAlignment.Left;
        }

        public virtual VerticalAlignment getLabelVerticalAlignment()
        {
            return VerticalAlignment.Stretch;
        }

        public virtual int getLabelWidth()
        {
            return 125;
        }

        public HorizontalAlignment getListContentHorizontalAlignment()
        {
            return HorizontalAlignment.Stretch;
        }

        public virtual Color getListBorderColor()
        {
            if (Application.Current.RequestedTheme.Equals(ApplicationTheme.Dark))
            {
                return Colors.White;
            }
            else
            {
                return Colors.Black;
            }
        }

        public virtual float getListBorderWidth()
        {
            return 1;
        }

        public virtual int getListContentWidth()
        {
            return -1; //see ListContentHorizontalAlignment
        }

        public virtual int getListHeight()
        {
            return 200;
        }

        public virtual HorizontalAlignment getListHorizontalAlignment()
        {
            return HorizontalAlignment.Stretch;
        }

        public virtual Color getListItemBackgroundColor()
        {
            return Colors.Transparent;
        }

        public virtual Color getListItemNameColor()
        {
            return Colors.LightSkyBlue;
        }

        public virtual FontFamily getListItemNameFont()
        {
            return new FontFamily("Segoe UI");
        }

        public virtual int getListItemNamePaddingBottom()
        {
            return 0;
        }

        public virtual int getListItemNamePaddingLeft()
        {
            return 10;
        }

        public virtual int getListItemNamePaddingRight()
        {
            return 0;
        }

        public virtual int getListItemNamePaddingTop()
        {
            return 0;
        }

        public virtual int getListItemNameSize()
        {
            return 20;
        }

        public virtual int getListItemsTextSize()
        {
            return 16;
        }

        public virtual Color getListItemTextColor()
        {
            if (Application.Current.RequestedTheme.Equals(ApplicationTheme.Dark))
            {
                return Colors.White;
            }
            else
            {
                return Colors.Black;
            }
        }

        public virtual FontFamily getListItemTextFont()
        {
            return new FontFamily("Segoe UI");
        }

        public virtual int getListItemTextPaddingBottom()
        {
            return 3;
        }

        public virtual int getListItemTextPaddingLeft()
        {
            return 10;
        }

        public virtual int getListItemTextPaddingRight()
        {
            return 5;
        }

        public virtual int getListItemTextPaddingTop()
        {
            return 0;
        }

        public virtual VerticalAlignment getListVerticalAlignment()
        {
            return VerticalAlignment.Stretch;
        }

        public virtual int getListWidth()
        {
            return -1; //see ListHorizontal
        }

        public virtual Color getValidationColor()
        {
            return Colors.Red;
        }

        public virtual FontFamily getValidationFont()
        {
            return new FontFamily("Segoe UI");
        }

        public virtual int getValidationFontSize()
        {
            return 18;
        }

        public virtual bool isListItemNameLabelVisible()
        {
            return true;
        }

        public virtual bool isListItemTextLabelsVisible()
        {
            return true;
        }

        public virtual bool isListScrollBarAlwaysVisible()
        {
            return true;
        }
    }
}
