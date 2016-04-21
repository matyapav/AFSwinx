package cz.cvut.fel.matyapav.showcase.skins;

import android.content.Context;

import cz.cvut.fel.matyapav.afandroid.builders.skins.DefaultSkin;

/**
 * Skin for absence management form
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class AbsenceManagementFormSkin extends DefaultSkin {

    public AbsenceManagementFormSkin(Context context) {
        super(context);
    }

    @Override
    public int getInputWidth() {
        return convertDpToPixels(120, getContext());
    }

    @Override
    public int getLabelWidth() {
        return convertDpToPixels(60, getContext());
    }

}
