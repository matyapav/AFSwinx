package cz.cvut.fel.matyapav.showcase.skins;

import android.content.Context;

import cz.cvut.fel.matyapav.afandroid.builders.skins.DefaultSkin;

/**
 * Skin for create absence form
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class CreateAbsenceFormSkin extends DefaultSkin {

    public CreateAbsenceFormSkin(Context context) {
        super(context);
    }

    @Override
    public int getLabelWidth() {
        return convertDpToPixels(100, getContext());
    }

    @Override
    public int getInputWidth() {
        return convertDpToPixels(200, getContext());
    }

}
