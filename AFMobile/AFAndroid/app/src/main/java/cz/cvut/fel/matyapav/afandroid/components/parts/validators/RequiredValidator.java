package cz.cvut.fel.matyapav.afandroid.components.parts.validators;

import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.tomscz.afrest.commons.SupportedWidgets;
import com.tomscz.afrest.rest.dto.AFValidationRule;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Validates REQUIRED rule, which checks field is filled.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class RequiredValidator implements AFValidator {

    @Override
    public boolean validate(AFField field, StringBuilder errorMsgs, AFValidationRule rule) {
        boolean validationIsFine = true;
        if(Utils.isFieldWritable(field.getFieldInfo().getWidgetType()) || field.getFieldInfo().getWidgetType().equals(SupportedWidgets.CALENDAR)){
            EditText textfield = (EditText) field.getFieldView();
            if (textfield.getText() == null || textfield.getText().toString().trim().isEmpty()) {
                validationIsFine = false;
            }
        }
        if(field.getFieldInfo().getWidgetType().equals(SupportedWidgets.OPTION)){
            RadioGroup radioGroup = (RadioGroup) field.getFieldView();
            if(radioGroup.getCheckedRadioButtonId() == -1){
                validationIsFine = false;
            }
        }
        if(field.getFieldInfo().getWidgetType().equals(SupportedWidgets.DROPDOWNMENU)){
            Spinner spinner = (Spinner) field.getFieldView();
            if(spinner.getSelectedItem() == null){
                validationIsFine = false;
            }
        }
        if(!validationIsFine){
            errorMsgs.append(Localization.translate("validation.required"));
        }
        return validationIsFine;
    }
}
