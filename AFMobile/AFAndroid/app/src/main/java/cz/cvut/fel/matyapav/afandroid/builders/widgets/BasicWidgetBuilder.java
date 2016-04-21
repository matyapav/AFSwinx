package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;

/**
 * Basic implementation of {@link AbstractWidgetBuilder}. Defines that widget builders needs skin
 * and information about field. Every concrete implementation of widget builder should extends this class.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
abstract class BasicWidgetBuilder implements AbstractWidgetBuilder {

    private Skin skin;
    private AFField field;

    public BasicWidgetBuilder(Skin skin, AFField field) {
        this.skin = skin;
        this.field = field;
    }

    public Skin getSkin() {
        return skin;
    }

    public AFField getField() {
        return field;
    }
}
