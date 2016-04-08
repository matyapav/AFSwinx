package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import android.app.Activity;
import android.view.View;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;

/**
 * Created by Pavel on 14.02.2016.
 */
public interface AbstractWidgetBuilder {

    View buildFieldView(Activity activity);

    void setData(Object value);

    Object getData();
}
