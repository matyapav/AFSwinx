package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.tomscz.afrest.rest.dto.AFFieldInfo;
import com.tomscz.afrest.rest.dto.AFOptions;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.matyapav.afandroid.R;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;

/**
 * Created by Pavel on 15.02.2016.
 */
public class DropDownWidgetBuilder extends BasicWidgetBuilder {

    public DropDownWidgetBuilder(Skin skin, AFFieldInfo properties) {
        super(skin, properties);
    }

    @Override
    public View buildFieldView(Activity activity) {
        Spinner spinner = new Spinner(activity);
        if(convertOptionsIntoList() != null) {
            ArrayAdapter<String> dataAdapter = new MySpinnerAdapter<>(activity,
                    R.layout.support_simple_spinner_dropdown_item,
                    convertOptionsIntoList(), getSkin());
            spinner.setAdapter(dataAdapter);
        }
        if(getProperties().getReadOnly()){
            spinner.setEnabled(false);
        }
        return spinner;
    }

    @Override
    public void setData(AFField field, Object value) {
        Spinner spinner = (Spinner) field.getFieldView();
        if(value == null){
            spinner.setSelection(0);
            field.setActualData(spinner.getSelectedItem());
            return;
        }
        if(value.toString().equals("true")){
            value = Localization.translate("option.yes");
        }else if(value.toString().equals("false")){
            value = Localization.translate("option.no");
        }
        if(field.getFieldInfo().getOptions() != null) {
            for (AFOptions option : field.getFieldInfo().getOptions()) {
                if(option.getKey().equals(value)){
                    value = Localization.translate(option.getValue());
                    break;
                }
            }
        }
        for (int i = 0; i < spinner.getCount(); i++) {
            if(spinner.getItemAtPosition(i).toString().equals(value)){
                spinner.setSelection(i);
                field.setActualData(spinner.getSelectedItem());
                return;
            }
        }
        field.setActualData(value);
    }

    @Override
    public Object getData(AFField field) {
        Spinner spinner = (Spinner) field.getFieldView();
        if(field.getFieldInfo().getOptions() != null) {
            for (AFOptions option : field.getFieldInfo().getOptions()) {
                if(Localization.translate(option.getValue()).equals(spinner.getSelectedItem().toString())){
                    return option.getKey();
                }
            }
        }
        if(spinner.getSelectedItem().toString().equals(Localization.translate("option.yes"))){
            return true;
        }else if(spinner.getSelectedItem().toString().equals(Localization.translate("option.no"))){
            return false;
        }else {
            return spinner.getSelectedItem().toString();
        }
    }

    private List<String> convertOptionsIntoList(){
        List<String> list = new ArrayList<>();
        int i = 0;
        if(getProperties().getOptions() != null){
            for (AFOptions option : getProperties().getOptions()) {
                if(option.getValue().toString().equals("true")){
                    list.add(Localization.translate("option.yes"));
                }else if(option.getValue().toString().equals("false")){
                    list.add(Localization.translate("option.no"));
                }else {
                    list.add(Localization.translate(option.getValue()));
                }
            }
            return list;
        }
        return null;
    }

    private static class MySpinnerAdapter<T> extends ArrayAdapter<T> {

        Skin skin;

        private MySpinnerAdapter(Context context, int resource, List<T> items, Skin skin) {
            super(context, resource, items);
            this.skin = skin;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTextColor(skin.getFieldColor());
            view.setTypeface(skin.getFieldFont());
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTextColor(skin.getFieldColor());
            view.setTypeface(skin.getFieldFont());
            return view;
        }
    }
}
