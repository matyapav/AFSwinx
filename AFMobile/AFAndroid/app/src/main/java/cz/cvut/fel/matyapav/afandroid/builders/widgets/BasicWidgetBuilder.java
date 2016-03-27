package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import com.tomscz.afrest.rest.dto.AFFieldInfo;

import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;

/**
 * Created by Pavel on 24.02.2016.
 */
abstract class BasicWidgetBuilder implements AbstractWidgetBuilder {

    private Skin skin;
    private AFFieldInfo properties;

    public BasicWidgetBuilder(Skin skin, AFFieldInfo properties) {
        this.skin = skin;
        this.properties = properties;
    }

    public Skin getSkin() {
        return skin;
    }

    public AFFieldInfo getProperties() {
        return properties;
    }
}
