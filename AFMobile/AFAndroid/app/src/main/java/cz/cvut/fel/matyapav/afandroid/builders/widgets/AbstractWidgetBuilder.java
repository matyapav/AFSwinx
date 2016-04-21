package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import android.app.Activity;
import android.view.View;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;

/**
 * Template for widget builders and it defines methods which every widget builder should have.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public interface AbstractWidgetBuilder {

    /**
     * This method is used for build graphical representation of widget - field's active element,
     * that means without validation or label elements.
     * @param activity is needed to be able to dynamically create a component
     * @return graphical representation of field's active element
     */
    View buildFieldView(Activity activity);

    /**
     * Sets data to widget - field's active element
     * @param value data to set (usually String)
     */
    void setData(Object value);

    /**
     * Gets data from widget - field's active element
     * @return data from widget
     */
    Object getData();
}
