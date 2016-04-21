package cz.cvut.fel.matyapav.afandroid.components.parts.validators;

import android.widget.EditText;

import com.tomscz.afrest.rest.dto.AFValidationRule;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Validates MAXLENGTH rule, which checks if number of chars is less or equal than value specified in rule.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class MaxCharsValidator implements AFValidator {

    @Override
    public boolean validate(AFField field, StringBuilder errorMsgs, AFValidationRule rule) {
        boolean validationIsFine = true;
        if(Utils.isFieldWritable(field.getFieldInfo().getWidgetType())){
            EditText textfield = (EditText) field.getFieldView();
            if (textfield.getText() != null && textfield.getText().toString().length() > Integer.parseInt(rule.getValue())) {
                validationIsFine = false;
            }
        }
        if(!validationIsFine){
            errorMsgs.append(Localization.translate("validation.maxchars"));
        }
        return true;
    }
}
