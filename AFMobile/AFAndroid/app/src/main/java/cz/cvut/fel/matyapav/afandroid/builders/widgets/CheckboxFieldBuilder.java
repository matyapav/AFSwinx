package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.components.parts.FieldInfo;

/**
 * Created by Pavel on 15.02.2016.
 */
public class CheckboxFieldBuilder implements BasicBuilder{

    @Override
    public View buildFieldView(Activity activity) {
        CheckBox checkBox = new CheckBox(activity);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return checkBox;
    }

    @Override
    public void setData(AFField field, Object value) {
        CheckBox box = (CheckBox) field.getFieldView();
        box.setChecked(Boolean.valueOf(value.toString()));
    }

    @Override
    public Object getData(AFField field) {
        CheckBox box = (CheckBox) field.getFieldView();
        return String.valueOf(box.isChecked());
    }
}