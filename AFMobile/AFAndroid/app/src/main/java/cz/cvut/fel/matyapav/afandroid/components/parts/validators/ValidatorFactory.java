package cz.cvut.fel.matyapav.afandroid.components.parts.validators;

import com.tomscz.afrest.commons.SupportedValidations;
import com.tomscz.afrest.rest.dto.AFValidationRule;

/**
 * Factory class to determine which validator should be picked based on validation rule.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class ValidatorFactory {

    private static ValidatorFactory instance = null;


    /**
     * Gets instance of validator factory.
     * @return instance of validator factory
     */
    public static synchronized ValidatorFactory getInstance() {
        if(instance == null){
            instance = new ValidatorFactory();
        }
        return instance;
    }

    /**
     * Gets corresponding validator for specific validation rule.
     *
     * @param rule specific validation rule
     * @return corresponding validator
     */
    public AFValidator getValidator(AFValidationRule rule){
        if (rule.getValidationType().equals(SupportedValidations.REQUIRED) && Boolean.valueOf(rule.getValue())) {
            return new RequiredValidator();
        }
        if (rule.getValidationType().equals(SupportedValidations.MAXLENGTH)) {
            return new MaxCharsValidator();
        }
        if (rule.getValidationType().equals(SupportedValidations.MAX)){
            return new MaxValueValidator();
        }
        if (rule.getValidationType().equals(SupportedValidations.MIN)){
            return new MinValueValidator();
        }
        if(rule.getValidationType().equals(SupportedValidations.LESSTHAN)){
            return new LessThanValidator();
        }
        System.err.println("VALIDATOR FOR "+rule.getValidationType()+" NOT FOUND");
        return null;
    }
}
