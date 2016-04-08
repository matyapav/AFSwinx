package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import com.tomscz.afrest.rest.dto.AFFieldInfo;

import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;

/**
 * Created by Pavel on 24.02.2016.
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
