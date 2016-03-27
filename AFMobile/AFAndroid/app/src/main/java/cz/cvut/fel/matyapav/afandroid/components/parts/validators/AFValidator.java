package cz.cvut.fel.matyapav.afandroid.components.parts.validators;

import com.tomscz.afrest.rest.dto.AFValidationRule;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;

/**
 * Created by Pavel on 14.02.2016.
 */
public interface AFValidator {

    boolean validate(AFField field, StringBuilder errorMsgs, AFValidationRule rule);
}
