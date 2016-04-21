package cz.cvut.fel.matyapav.afandroid.components.parts.validators;

import com.tomscz.afrest.commons.SupportedWidgets;
import com.tomscz.afrest.rest.dto.AFValidationRule;

import java.util.Date;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.components.types.AFForm;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Validates LESSTHAN rule, which checks if value in one field is less or equal than value in other field.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class LessThanValidator implements AFValidator {

    @Override
    public boolean validate(AFField field, StringBuilder errorMsgs, AFValidationRule rule) {
        boolean validationIsFine = true;
        Object otherData = ((AFForm) field.getParent()).getDataFromFieldWithId(rule.getValue());
        if(otherData != null) {
            if (Utils.isFieldNumberField(field)) {
                //TODO pro cisla
            }
            if (field.getFieldInfo().getWidgetType().equals(SupportedWidgets.CALENDAR)) {
                Object fieldData = ((AFForm) field.getParent()).getDataFromFieldWithId(field.getId());
                if(fieldData!= null) {
                    Date date = Utils.parseDate(fieldData.toString());
                    Date otherDate = Utils.parseDate(otherData.toString());
                    if(date.after(otherDate)){
                        validationIsFine = false;
                    }
                }
            }
            if (!validationIsFine) {
                String otherFieldLabelText = (field.getParent()).getFieldById(rule.getValue()).getFieldInfo().getLabel();
                errorMsgs.append(Localization.translate("validation.lessthan") + " "
                        + Localization.translate(otherFieldLabelText));
            }
        }
        return validationIsFine;
    }
}
