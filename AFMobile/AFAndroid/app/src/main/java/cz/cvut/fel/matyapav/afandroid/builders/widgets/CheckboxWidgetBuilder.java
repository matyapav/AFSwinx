package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.tomscz.afrest.rest.dto.AFFieldInfo;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;

/**
 * Created by Pavel on 15.02.2016.
 */
public class CheckboxWidgetBuilder extends BasicWidgetBuilder {

    public CheckboxWidgetBuilder(Skin skin, AFFieldInfo properties) {
        super(skin, properties);
    }

    @Override
    public View buildFieldView(Activity activity) {
        CheckBox checkBox = new CheckBox(activity);
        checkBox.setTextColor(getSkin().getFieldColor());
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if(getProperties().getReadOnly()){
            checkBox.setEnabled(false);
        }
        return checkBox;
    }

    @Override
    public void setData(AFField field, Object value) {
        CheckBox box = (CheckBox) field.getFieldView();
        box.setChecked(Boolean.valueOf(value.toString()));
        field.setActualData(value);
    }

    @Override
    public Object getData(AFField field) {
        CheckBox box = (CheckBox) field.getFieldView();
        return String.valueOf(box.isChecked());
    }
}
