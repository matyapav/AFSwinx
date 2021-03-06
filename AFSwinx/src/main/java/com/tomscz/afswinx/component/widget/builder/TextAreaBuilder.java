package com.tomscz.afswinx.component.widget.builder;

import javax.swing.JTextArea;

import com.tomscz.afrest.commons.SupportedWidgets;
import com.tomscz.afrest.rest.dto.AFFieldInfo;
import com.tomscz.afswinx.component.AFSwinxBuildException;
import com.tomscz.afswinx.component.panel.AFSwinxPanel;

/**
 * This is builder for TextArea component.
 * 
 * @author Martin Tomasek (martin@toms-cz.com)
 * 
 * @since 1.0.0.
 */
public class TextAreaBuilder extends InputBuilder {

    public static final int rows = 3;
    public static final int columns = 20;

    public TextAreaBuilder() {
        widgetType = SupportedWidgets.TEXTAREA;
    }

    @Override
    public AFSwinxPanel buildComponent(AFFieldInfo field) throws IllegalArgumentException,
            AFSwinxBuildException {
        super.buildBase(field);
        // And input text field
        JTextArea textArea = new JTextArea();
        layoutBuilder.addComponent(textArea);
        coreComponent = textArea;
        customizeComponent(textArea, field);
        // Create panel which holds all necessary informations
        AFSwinxPanel afPanel =
                new AFSwinxPanel(field.getId(), field.getWidgetType(), textArea, fieldLabel,
                        message);
        // Build layout on that panel
        layoutBuilder.buildLayout(afPanel);
        // Add validations
        super.crateValidators(afPanel, field);
        return afPanel;
    }

}
