package com.tomscz.afswinx.component.builders;

import javax.swing.JComponent;

import com.tomscz.afrest.commons.SupportedWidgets;
import com.tomscz.afrest.rest.dto.AFFieldInfo;
import com.tomscz.afrest.rest.dto.AFOptions;
import com.tomscz.afrest.rest.dto.data.AFData;
import com.tomscz.afswinx.component.builders.abstraction.BaseComponentsBuilder;
import com.tomscz.afswinx.component.panel.AFSwinxPanel;
import com.tomscz.afswinx.swing.component.AFOptionToAFSwinxOption;
import com.tomscz.afswinx.swing.component.AFComponentDataHolder;
import com.tomscz.afswinx.swing.component.JCheckBox;

/**
 * This is builder which can build {@link JCheckBox} component.
 * 
 * @author Martin Tomasek (martin@toms-cz.com)
 * 
 * @since 1.0.0.
 */
public class CheckBoxBuilder extends BaseComponentsBuilder {

    public CheckBoxBuilder() {
        widgetType = SupportedWidgets.CHECKBOX;
    }

    @Override
    public AFSwinxPanel buildComponent(AFFieldInfo field) throws IllegalArgumentException {
        super.buildBase(field);
        // Create panel which holds all necessary informations
        AFSwinxPanel afPanel =
                new AFSwinxPanel(field.getId(), field.getWidgetType(), fieldLabel, message);
        if (field.getOptions() != null) {
            AFOptionToAFSwinxOption converter = new AFOptionToAFSwinxOption();
            for (AFOptions option : field.getOptions()) {
                AFComponentDataHolder optionToAdd = converter.convert(option, localization);
                if (optionToAdd != null) {
                    JCheckBox<AFComponentDataHolder> checkBox =
                            new JCheckBox<AFComponentDataHolder>(optionToAdd);
                    checkBox.setText(optionToAdd.getValueToDisplay());
                    layoutBuilder.addComponent(checkBox);
                    afPanel.addDataHolderComponent(checkBox);
                }
            }
        } else {
            AFComponentDataHolder option = new AFComponentDataHolder("true", "true");
            JCheckBox<AFComponentDataHolder> checkBox =
                    new JCheckBox<AFComponentDataHolder>(option);
            layoutBuilder.addComponent(checkBox);
            afPanel.addDataHolderComponent(checkBox);
        }
        // Build layout on that panel
        layoutBuilder.buildLayout(afPanel);
        // Add validations
        super.crateValidators(afPanel, field);
        return afPanel;
    }

    @Override
    public void setData(AFSwinxPanel panel, AFData data) {
        if (panel.getDataHolder() != null && data.getValue() != null
                && !panel.getDataHolder().isEmpty()) {
            for (JComponent component : panel.getDataHolder()) {
                @SuppressWarnings("unchecked")
                JCheckBox<AFComponentDataHolder> checkBox =
                        (JCheckBox<AFComponentDataHolder>) component;
                AFComponentDataHolder dataHolder = checkBox.getDataHolder();
                if (dataHolder.getValue().equals(data.getValue())) {
                    checkBox.setSelected(true);
                }
            }
        }
    }

    @Override
    public Object getData(AFSwinxPanel panel) {
        if (panel.getDataHolder() != null && !panel.getDataHolder().isEmpty()) {
            for (JComponent component : panel.getDataHolder()) {
                @SuppressWarnings("unchecked")
                JCheckBox<AFComponentDataHolder> checkBox =
                        (JCheckBox<AFComponentDataHolder>) component;
                AFComponentDataHolder dataHolder = checkBox.getDataHolder();
                if (checkBox.isSelected()) {
                    return String.valueOf(dataHolder.getKey());
                }
            }
        }
        return null;
    }

}