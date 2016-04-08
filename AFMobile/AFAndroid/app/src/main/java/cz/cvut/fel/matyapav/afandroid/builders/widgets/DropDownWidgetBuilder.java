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

    public DropDownWidgetBuilder(Skin skin, AFField field) {
        super(skin, field);
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
        if(getField().getFieldInfo().getReadOnly()){
            spinner.setEnabled(false);
        }
        return spinner;
    }

    @Override
    public void setData(Object value) {
        Spinner spinner = (Spinner) getField().getFieldView();
        if(value == null){
            spinner.setSelection(0);
            getField().setActualData(spinner.getSelectedItem());
            return;
        }
        if(value.toString().equals("true")){
            value = Localization.translate("option.yes");
        }else if(value.toString().equals("false")){
            value = Localization.translate("option.no");
        }
        if(getField().getFieldInfo().getOptions() != null) {
            for (AFOptions option : getField().getFieldInfo().getOptions()) {
                if(option.getKey().equals(value)){
                    value = Localization.translate(option.getValue());
                    break;
                }
            }
        }
        for (int i = 0; i < spinner.getCount(); i++) {
            if(spinner.getItemAtPosition(i).toString().equals(value)){
                spinner.setSelection(i);
                getField().setActualData(spinner.getSelectedItem());
                return;
            }
        }
        getField().setActualData(value);
    }

    @Override
    public Object getData() {
        Spinner spinner = (Spinner) getField().getFieldView();
        if(getField().getFieldInfo().getOptions() != null) {
            for (AFOptions option : getField().getFieldInfo().getOptions()) {
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
        if(getField().getFieldInfo().getOptions() != null){
            for (AFOptions option : getField().getFieldInfo().getOptions()) {
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
