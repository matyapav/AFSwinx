using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Media;

namespace AFWinPhone.builders.skins
{
    /// <summary>
    /// This is skin interfaces. This interface is used to skin component based on user setting.
    /// </summary>
    public interface Skin
    {

        int getComponentMarginLeft();
        int getComponentMarginRight();
        int getComponentMarginTop();
        int getComponentMarginBottom();

        //forms
        Color getLabelColor();

        Color getFieldColor();

        Color getValidationColor();

        int getInputWidth();
        HorizontalAlignment getInputHorizontalAlignment();

        FontFamily getValidationFont();
        int getValidationFontSize();

        FontFamily getFieldFont();
        int getFieldFontSize();

        FontFamily getLabelFont();
        int getLabelFontSize();

        int getLabelWidth();
        HorizontalAlignment getLabelHorizontalAlignment();
        int getLabelHeight();
        VerticalAlignment getLabelVerticalAlignment();

        //lists
        int getListWidth();
        int getListHeight();

        HorizontalAlignment getListHorizontalAlignment();
        VerticalAlignment getListVerticalAlignment();
  
        Color getListItemBackgroundColor();
        Color getListItemNameColor();
        Color getListItemTextColor();
        FontFamily getListItemNameFont();
        FontFamily getListItemTextFont();
        int getListItemNameSize();
        int getListItemsTextSize();
        bool isListItemNameLabelVisible();
        bool isListItemTextLabelsVisible();

        bool isListScrollBarAlwaysVisible();

        int getListItemTextPaddingLeft();
        int getListItemTextPaddingRight();
        int getListItemTextPaddingTop();
        int getListItemTextPaddingBottom();

        int getListItemNamePaddingLeft();

        int getListItemNamePaddingRight();

        int getListItemNamePaddingTop();

        int getListItemNamePaddingBottom();

        int getListContentWidth();
        HorizontalAlignment getListContentHorizontalAlignment();

        Color getListBorderColor();
        float getListBorderWidth();
    }
}
