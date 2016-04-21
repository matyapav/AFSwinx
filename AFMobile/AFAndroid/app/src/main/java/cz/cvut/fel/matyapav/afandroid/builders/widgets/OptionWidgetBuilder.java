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
 * Builds radio group widget, where user can choose from multiple options.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class OptionWidgetBuilder extends BasicWidgetBuilder {

    public OptionWidgetBuilder(Skin skin, AFField field) {
        super(skin, field);
    }

    @Override
    public View buildFieldView(Activity activity) {
        RadioGroup radioGroup = new RadioGroup(activity);
        radioGroup.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        radioGroup.setOrientation(RadioGroup.VERTICAL); //TODO change according to layout parameters
        int numberOfOptions;
        if(getField().getFieldInfo().getOptions() != null && !getField().getFieldInfo().getOptions().isEmpty()) {
            numberOfOptions = getField().getFieldInfo().getOptions().size();
            RadioButton[] options = new RadioButton[numberOfOptions];
            for (int i = 0; i < numberOfOptions; i++) {
                options[i] = new RadioButton(activity);
                options[i].setTextColor(getSkin().getFieldColor());
                options[i].setTypeface(getSkin().getFieldFont());
                options[i].setText(getField().getFieldInfo().getOptions().get(i).getValue());
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
        if(getField().getFieldInfo().getReadOnly()){
            radioGroup.setEnabled(false);
        }
        return radioGroup;
    }

    //TODO REWRITE
    @Override
    public void setData( Object value) {

        RadioGroup group = (RadioGroup) getField().getFieldView();
        if(value == null){
            group.clearCheck();
            getField().setActualData(value);
            return;
        }
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton btn = (RadioButton) group.getChildAt(i);
            if(btn.getText().equals(value)
                    || (Boolean.valueOf(value.toString()) == true && i==0)
                    || (Boolean.valueOf(value.toString()) == false && i==1)){
                btn.setChecked(true);
                getField().setActualData(value);
                break;
            }
        };

    }

    @Override
    public Object getData() {
        RadioGroup group = (RadioGroup)  getField().getFieldView();
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
