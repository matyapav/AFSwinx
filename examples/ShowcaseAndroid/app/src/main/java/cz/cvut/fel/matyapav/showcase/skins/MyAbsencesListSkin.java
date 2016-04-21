package cz.cvut.fel.matyapav.showcase.skins;

import android.content.Context;
import android.widget.AbsListView;

/**
 * Skin for my absences list
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class MyAbsencesListSkin extends ListSkin {


    public MyAbsencesListSkin(Context context) {
        super(context);
    }

    @Override
    public int getListHeight() {
        return AbsListView.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getComponentMarginTop() {
        return convertDpToPixels(20, getContext());
    }
}




