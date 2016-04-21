package cz.cvut.fel.matyapav.afandroid.components.parts.validators;

import com.tomscz.afrest.rest.dto.AFValidationRule;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;

/**
 * Interface for all rule validators.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public interface AFValidator {

    /**
     * Validates specific validation rule of specific field. Appends error message if validation failed.
     * @param field field on which should be validation checked
     * @param errorMsgs string builder to potentially append error message
     * @param rule specific validation rule which should be checked
     * @return true if validation passed, false otherwise
     */
    boolean validate(AFField field, StringBuilder errorMsgs, AFValidationRule rule);
}
