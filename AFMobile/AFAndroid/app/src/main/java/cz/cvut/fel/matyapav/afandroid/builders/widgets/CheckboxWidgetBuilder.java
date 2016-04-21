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
 * This class is responsible for building checkbox element.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class CheckboxWidgetBuilder extends BasicWidgetBuilder {

    public CheckboxWidgetBuilder(Skin skin, AFField field) {
        super(skin, field);
    }

    @Override
    public View buildFieldView(Activity activity) {
        CheckBox checkBox = new CheckBox(activity);
        checkBox.setTextColor(getSkin().getFieldColor());
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if(getField().getFieldInfo().getReadOnly()){
            checkBox.setEnabled(false);
        }
        return checkBox;
    }

    @Override
    public void setData(Object value) {
        CheckBox box = (CheckBox) getField().getFieldView();
        box.setChecked(Boolean.valueOf(value.toString()));
        getField().setActualData(value);
    }

    @Override
    public Object getData() {
        CheckBox box = (CheckBox) getField().getFieldView();
        return String.valueOf(box.isChecked());
    }
}
