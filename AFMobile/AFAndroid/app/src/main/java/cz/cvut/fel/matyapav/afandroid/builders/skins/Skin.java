package cz.cvut.fel.matyapav.afandroid.builders.skins;

import android.graphics.Typeface;
import android.view.ViewGroup;

/**
 * This is skin interfaces. This interface is used to skin component based on user setting.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public interface Skin {

    //common
    ViewGroup.LayoutParams getTopLayoutParams();

    int getComponentMarginLeft();
    int getComponentMarginRight();
    int getComponentMarginTop();
    int getComponentMarginBottom();

    //forms
    int getLabelColor();

    int getFieldColor();

    int getValidationColor();

    int getInputWidth();

    Typeface getValidationFont();

    Typeface getFieldFont();

    Typeface getLabelFont();

    int getLabelWidth();

    int getLabelHeight();

    //tables
    int getTableHeight();
    int getHeaderRowHeight();
    int getContentRowHeight();

    int getHeaderRowBackgroundColor();
    int getHeaderRowTextColor();
    int getContentBackgroundColor();
    int getContentTextColor();
    int getBorderColor();

    int getCellPaddingLeft();
    int getCellPaddingRight();
    int getCellPaddingTop();
    int getCellPaddingBottom();

    int getHeaderRowGravity();
    int getContentGravity();

    int getBorderWidth();

    //lists
    int getListWidth();
    int getListHeight();
    int getListItemBackgroundColor();
    int getListItemNameColor();
    int getListItemTextColor();
    Typeface getListItemNameFont();
    Typeface getListItemTextFont();
    int getListItemNameSize();
    int getListItemsTextSize();
    boolean isListItemNameLabelVisible();
    boolean isListItemTextLabelsVisible();
    boolean isListScrollBarAlwaysVisible();

    int getListItemTextPaddingLeft();
    int getListItemTextPaddingRight();
    int getListItemTextPaddingTop();
    int getListItemTextPaddingBottom();

    int getListItemNamePaddingLeft();

    int getListItemNamePaddingRight();

    int getListItemNamePaddingTop();

    int getListItemNamePaddingBottom();

    int getListContentWidth();

    int getListBorderColor();
    float getListBorderWidth();
}
