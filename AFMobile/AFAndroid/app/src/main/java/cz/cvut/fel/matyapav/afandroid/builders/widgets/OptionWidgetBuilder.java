package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tomscz.afrest.rest.dto.AFFieldInfo;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;

/**
 * Created by Pavel on 15.02.2016.
 */
public class OptionWidgetBuilder extends BasicWidgetBuilder {

    public OptionWidgetBuilder(Skin skin, AFFieldInfo properties) {
        super(skin, properties);
    }

    @Override
    public View buildFieldView(Activity activity) {
        RadioGroup radioGroup = new RadioGroup(activity);
        radioGroup.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        radioGroup.setOrientation(RadioGroup.VERTICAL); //TODO change according to layout parameters
        int numberOfOptions;
        if(getProperties().getOptions() != null && !getProperties().getOptions().isEmpty()) {
            numberOfOptions = getProperties().getOptions().size();
            RadioButton[] options = new RadioButton[numberOfOptions];
            for (int i = 0; i < numberOfOptions; i++) {
                options[i] = new RadioButton(activity);
                options[i].setTextColor(getSkin().getFieldColor());
                options[i].setTypeface(getSkin().getFieldFont());
                options[i].setText(getProperties().getOptions().get(i).getValue());
                options[i].setId(i + 100);
                radioGroup.addView(options[i]);
            }
        }else{
            //YES NO OPTIONS
            numberOfOptions = 2;
            RadioButton[] options = new RadioButton[numberOfOptions];
            options[0] = new RadioButton(activity);
            options[0].setTextColor(getSkin().getFieldColor());
            options[0].setTypeface(getSkin().getFieldFont());
            options[0].setText(Localization.translate("option.yes"));
            options[1] = new RadioButton(activity);
            options[1].setTextColor(getSkin().getFieldColor());
            options[1].setTypeface(getSkin().getFieldFont());
            options[1].setText(Localization.translate("option.no"));
            radioGroup.addView(options[0]);
            radioGroup.addView(options[1]);
        }
        if(getProperties().getReadOnly()){
            radioGroup.setEnabled(false);
        }
        return radioGroup;
    }

    //TODO REWRITE
    @Override
    public void setData(AFField field, Object value) {

        RadioGroup group = (RadioGroup) field.getFieldView();
        if(value == null){
            group.clearCheck();
            field.setActualData(value);
            return;
        }
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton btn = (RadioButton) group.getChildAt(i);
            if(btn.getText().equals(value)
                    || (Boolean.valueOf(value.toString()) == true && i==0)
                    || (Boolean.valueOf(value.toString()) == false && i==1)){
                btn.setChecked(true);
                field.setActualData(value);
                break;
            }
        };

    }

    @Override
    public Object getData(AFField field) {
        RadioGroup group = (RadioGroup) field.getFieldView();
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton btn = (RadioButton) group.getChildAt(i);
            if(btn.isChecked()){
                if(btn.getText().toString().equals(Localization.translate("option.yes"))){
                    return true;
                }else if(btn.getText().toString().equals(Localization.translate("option.no"))){
                    return false;
                }else {
                    return btn.getText().toString();
                }
            }
        }
        return null; //nothing is checked
    }
}
