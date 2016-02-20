package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.components.parts.FieldInfo;
import cz.cvut.fel.matyapav.afandroid.components.parts.FieldOption;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;

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
                android.support.design.R.layout.support_simple_spinner_dropdown_item, convertOptionsIntoList(activity));
        spinner.setAdapter(dataAdapter);
        return spinner;
    }

    @Override
    public void setData(AFField field, Object value) {
        Spinner spinner = (Spinner) field.getFieldView();
        for (int i = 0; i < spinner.getCount(); i++) {
            if(spinner.getItemAtPosition(i).toString().equals(value)){
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public Object getData(AFField field) {
        Spinner spinner = (Spinner) field.getFieldView();
        return spinner.getSelectedItem();
    }

    private List<String> convertOptionsIntoList(Activity activity){
        List<String> list = new ArrayList<>();
        int i = 0;
        for (FieldOption option:properties.getOptions()) {
            list.add(Localization.translate(option.getValue(),activity));
        }
        return list;
    }
}
