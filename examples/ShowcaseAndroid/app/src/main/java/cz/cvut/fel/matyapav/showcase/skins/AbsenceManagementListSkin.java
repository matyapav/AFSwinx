package cz.cvut.fel.matyapav.showcase.skins;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import cz.cvut.fel.matyapav.showcase.R;

/**
 * Skin for absence management list
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
*/
public class AbsenceManagementListSkin extends ListSkin{

    public AbsenceManagementListSkin(Context context) {
        super(context);
    }


    @Override
    public boolean isListItemNameLabelVisible() {
        return false;
    }

    @Override
    public int getListItemsTextSize() {
        return 14;
    }

    @Override
    public int getListItemNameColor() {
        return ContextCompat.getColor(getContext(), R.color.colorAccent2);
    }

    @Override
    public int getComponentMarginBottom() {
        return convertDpToPixels(30, getContext());
    }
}
