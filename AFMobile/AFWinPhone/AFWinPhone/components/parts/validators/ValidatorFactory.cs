using AFWinPhone.components.parts.validators;
using AFWinPhone.enums;
using AFWinPhone.utils;

namespace AFWinPhone.components.parts
{
    class ValidatorFactory
    {
        private static ValidatorFactory instance = null;


        public static ValidatorFactory getInstance()
        {
            if (instance == null)
            {
                instance = new ValidatorFactory();
            }
            return instance;
        }

        public AFValidator getValidator(AFValidationRule rule)
        {
            if (rule.getValidationType().Equals(SupportedValidations.REQUIRED.getValidationType()) && Utils.TryToConvertIntoBoolean(rule.getValue()))
            {
                return new RequiredValidator();
            }
            if (rule.getValidationType().Equals(SupportedValidations.MAXLENGTH.getValidationType()))
            {
                return new MaxCharsValidator();
            }
            if (rule.getValidationType().Equals(SupportedValidations.MAX.getValidationType()))
            {
                return new MaxValueValidator();
            }
            if (rule.getValidationType().Equals(SupportedValidations.MIN.getValidationType()))
            {
                return new MinValueValidator();
            }
            if (rule.getValidationType().Equals(SupportedValidations.LESSTHAN.getValidationType()))
            {
                return new LessThanValidator();
            }
            if (rule.getValidationType().Equals(SupportedValidations.NUMBER.getValidationType()))
            {
                return new NumberValidator();
            }
            //not found
            return null;
        }

    }
}
