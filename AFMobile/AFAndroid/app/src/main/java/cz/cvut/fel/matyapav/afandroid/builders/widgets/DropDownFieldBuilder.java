package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.matyapav.afandroid.components.parts.FieldInfo;
import cz.cvut.fel.matyapav.afandroid.components.parts.FieldOption;

/**
 * Created by Pavel on 15.02.2016.
 */
public class DropDownFieldBuilder implements BasicBuilder {

    private FieldInfo properties;

    public DropDownFieldBuilder(FieldInfo properties) {
        this.properties = properties;
    }

    @Override
    public View buildFieldView(Activity activity) {
        Spinner spinner = new Spinner(activity);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
                android.support.design.R.layout.support_simple_spinner_dropdown_item, convertOptionsIntoList());
        spinner.setAdapter(dataAdapter);
        return spinner;
    }

    private List<String> convertOptionsIntoList(){
        List<String> list = new ArrayList<>();
        int i = 0;
        for (FieldOption option:properties.getOptions()) {
            list.add(option.getValue());
        }
        return list;
    }
}
