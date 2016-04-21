package cz.cvut.fel.matyapav.showcase.skins;

import android.content.Context;

import cz.cvut.fel.matyapav.afandroid.builders.skins.DefaultSkin;

/**
 * Skin for contry form
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class CountryFormSkin extends DefaultSkin {

    public CountryFormSkin(Context context) {
        super(context);
    }

    @Override
    public int getInputWidth() {
        return convertDpToPixels(150, getContext());
    }

    @Override
    public int getLabelWidth() {
        return convertDpToPixels(80, getContext());
    }
}
