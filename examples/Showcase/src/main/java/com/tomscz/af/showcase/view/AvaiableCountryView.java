package com.tomscz.af.showcase.view;

import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.tomscz.af.showcase.application.ApplicationContext;
import com.tomscz.af.showcase.view.skin.MySkin;
import com.tomscz.afswinx.component.AFSwinx;
import com.tomscz.afswinx.component.AFSwinxBuildException;
import com.tomscz.afswinx.component.AFSwinxForm;
import com.tomscz.afswinx.component.AFSwinxTable;

public class AvaiableCountryView extends BaseScreen {

    private static final long serialVersionUID = 1L;

    public static final String COUNTRY_TABLE = "countryTable";
    public static final String COUNTRY_FORM = "countryForm";
    public static final String COUNTRY_TABLE_CONNECTION_KEY = "tableCountryPublic";
    public static final String COUNTRY_FORM_CONNECTION_KEY = "countryAdd";
    private JButton addCountryButton;
    private JButton chooseCountry;
    private JButton resetForm;

    public AvaiableCountryView() {
        intialize();
    }

    public void addAddCountryListener(ActionListener a) {
        if (addCountryButton != null) {
            this.addCountryButton.addActionListener(a);
        }
    }

    public void addChooseCountryListener(ActionListener a) {
        if (chooseCountry != null) {
            this.chooseCountry.addActionListener(a);
        }
    }

    public void addResetForm(ActionListener a) {
        if (resetForm != null) {
            this.resetForm.addActionListener(a);
        }
    }
    
    @Override
    protected JPanel createContent() {
        JPanel mainPanel = new JPanel();
        Box b1 = Box.createVerticalBox();
        InputStream connectionResource =
                getClass().getClassLoader().getResourceAsStream("connection.xml");
        try {
            AFSwinxTable table =
                    AFSwinx.getInstance()
                            .getTableBuilder()
                            .initBuilder(COUNTRY_TABLE, connectionResource,
                                    COUNTRY_TABLE_CONNECTION_KEY)
                            .setLocalization(ApplicationContext.getInstance().getLocalization())
                            .buildComponent();
            Box centerPanel = Box.createVerticalBox();
            centerPanel.setAlignmentX(CENTER_ALIGNMENT);
            HashMap<String, String> securityConstrains =
                    ApplicationContext.getInstance().getSecurityContext().getUserNameAndPasswodr();
            connectionResource = getClass().getClassLoader().getResourceAsStream("connection.xml");
            AFSwinxForm form =
                    AFSwinx.getInstance()
                            .getFormBuilder()
                            .initBuilder(COUNTRY_FORM, connectionResource,
                                    COUNTRY_FORM_CONNECTION_KEY, securityConstrains)
                            .setLocalization(ApplicationContext.getInstance().getLocalization())
                            .setSkin(new MySkin()).buildComponent();
            centerPanel.add(form);
            addCountryButton =
                    new JButton(Localization.getLocalizationText("avaiableCountryView.buttton.add"));
            addCountryButton.setAlignmentX(CENTER_ALIGNMENT);
            resetForm =
                    new JButton(
                            Localization.getLocalizationText("button.reset"));
            resetForm.setAlignmentX(CENTER_ALIGNMENT);
            Box buttonBox = Box.createHorizontalBox();
            buttonBox.add(addCountryButton);
            buttonBox.add(Box.createHorizontalStrut(60));
            buttonBox.add(resetForm);
            centerPanel.add(Box.createVerticalStrut(20));
            centerPanel.add(buttonBox);
            chooseCountry =
                    new JButton(
                            Localization.getLocalizationText("avaiableCountryView.buttton.choose"));
            chooseCountry.setAlignmentX(RIGHT_ALIGNMENT);
            Box centerBox = Box.createHorizontalBox();
            centerBox.add(Box.createHorizontalStrut(100));
            centerBox.add(centerPanel);
            centerBox.add(Box.createHorizontalStrut(100));
            b1.add(table);
            b1.add(chooseCountry);
            b1.add(Box.createVerticalStrut(40));
            b1.add(centerBox);
            b1.add(Box.createVerticalStrut(40));
            mainPanel.add(b1);
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            return mainPanel;
        } catch (AFSwinxBuildException e) {
            getDialogs().failed("afswinx.build.title.failed", "afswinx.build.text.failed",
                    e.getMessage());
        }
        return mainPanel;
    }

}