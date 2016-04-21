using AFWinPhone.components.parts.validators;
using AFWinPhone.enums;
using AFWinPhone.utils;

namespace AFWinPhone.components.parts
{
    /// <summary>
    /// Factory class to determine which validator should be picked based on validation rule.
    /// </summary>
    class ValidatorFactory
    {
        private static ValidatorFactory instance = null;

        /// <summary>
        /// Gets instance of validator factory.
        /// </summary>
        /// <returns>instance of validator factory</returns>
        public static ValidatorFactory getInstance()
        {
            if (instance == null)
            {
                instance = new ValidatorFactory();
            }
            return instance;
        }

        /// <summary>
        /// Gets corresponding validator for specific validation rule.
        /// </summary>
        /// <param name="rule">specific validation rule</param>
        /// <returns>corresponding validator</returns>
        public AFValidator getValidator(AFValidationRule rule)
        {
            if (rule.getValidationType().Equals(SupportedValidations.REQUIRED) && Utils.TryToConvertIntoBoolean(rule.getValue()))
            {
                return new RequiredValidator();
            }
            if (rule.getValidationType().Equals(SupportedValidations.MAXLENGTH))
            {
                return new MaxCharsValidator();
            }
            if (rule.getValidationType().Equals(SupportedValidations.MAX))
            {
                return new MaxValueValidator();
            }
            if (rule.getValidationType().Equals(SupportedValidations.MIN))
            {
                return new MinValueValidator();
            }
            if (rule.getValidationType().Equals(SupportedValidations.LESSTHAN))
            {
                return new LessThanValidator();
            }
            if (rule.getValidationType().Equals(SupportedValidations.NUMBER))
            {
                return new NumberValidator();
            }
            //not found
            return null;
        }

    }
}
