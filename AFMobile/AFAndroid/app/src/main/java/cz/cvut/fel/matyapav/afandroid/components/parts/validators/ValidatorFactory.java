package cz.cvut.fel.matyapav.afandroid.components.parts.validators;

import com.tomscz.afrest.commons.SupportedValidations;
import com.tomscz.afrest.rest.dto.AFValidationRule;

/**
 * Created by Pavel on 28.02.2016.
 */
public class ValidatorFactory {

    private static ValidatorFactory instance = null;


    public static synchronized ValidatorFactory getInstance() {
        if(instance == null){
            instance = new ValidatorFactory();
        }
        return instance;
    }

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
