package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import cz.cvut.fel.matyapav.afandroid.components.parts.FieldInfo;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;

/**
 * Created by Pavel on 24.02.2016.
 */
abstract class BasicWidgetBuilder implements AbstractWidgetBuilder {

    private Skin skin;
    private FieldInfo properties;

    public BasicWidgetBuilder(Skin skin, FieldInfo properties) {
        this.skin = skin;
        this.properties = properties;
    }

    public Skin getSkin() {
        return skin;
    }

    public FieldInfo getProperties() {
        return properties;
    }
}
