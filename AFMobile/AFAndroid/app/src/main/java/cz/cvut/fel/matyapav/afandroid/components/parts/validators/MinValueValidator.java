package cz.cvut.fel.matyapav.afandroid.components.parts.validators;

import android.widget.EditText;

import com.tomscz.afrest.rest.dto.AFValidationRule;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Created by Pavel on 15.02.2016.
 */
public class MinValueValidator implements AFValidator {

    @Override
    public boolean validate(AFField field, StringBuilder errorMsgs, AFValidationRule rule) {
        boolean validationIsFine = true;
        if(Utils.isFieldNumberField(field)){
            EditText numberField = (EditText) field.getFieldView();
            if(numberField.getText() != null && !numberField.getText().toString().isEmpty() &&
                    Double.valueOf(numberField.getText().toString()) < Double.valueOf(rule.getValue())){
                validationIsFine = false;
            }
        }
        if(!validationIsFine){
            errorMsgs.append(Localization.translate("validation.minval")+" "+rule.getValue());
        }
        return validationIsFine;
    }
}
