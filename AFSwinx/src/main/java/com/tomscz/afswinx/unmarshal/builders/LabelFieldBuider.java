package com.tomscz.afswinx.unmarshal.builders;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tomscz.afswinx.component.panel.AFSwinxPanel;
import com.tomscz.afswinx.rest.dto.AFFieldInfo;
import com.tomscz.afswinx.rest.dto.data.AFData;
import com.tomscz.afswinx.unmarshal.builders.abstraction.TwoComponentsBuilder;

public class LabelFieldBuider extends TwoComponentsBuilder {

    @Override
    public AFSwinxPanel buildComponent(AFFieldInfo field) throws IllegalArgumentException {
        if (!isBuildAvailable(field)) {
            throw new IllegalArgumentException("Input field couldn't be build for this field");
        }

        JPanel panel = new JPanel();
        JLabel fieldLabel = null;
        JLabel dataLabel = null;

        fieldLabel = super.buildSimpleLabel(field.getLabel());
        dataLabel = new JLabel();
        // TODO add layout based on label position, but its for top level class
        if (fieldLabel != null) {
            panel.add(fieldLabel);
        }

        panel.add(dataLabel);
        AFSwinxPanel afPanel =
                new AFSwinxPanel(field.getId(), field.getWidgetType(), dataLabel, fieldLabel);
        afPanel.add(panel);
        return afPanel;
    }

    @Override
    public void setData(AFSwinxPanel panel, AFData data) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getData(AFSwinxPanel panel) {
        // TODO Auto-generated method stub
        return null;
    }
}