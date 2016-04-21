using System.Text;

namespace AFWinPhone.components.parts.validators
{
    /// <summary>
    /// Interface for all rule validators.
    /// </summary>
    interface AFValidator
    {
        /// <summary>
        /// Validates specific validation rule of specific field. Appends error message if validation failed.
        /// </summary>
        /// <param name="field">field on which should be validation checked</param>
        /// <param name="errorMsgs">string builder to potentially append error message</param>
        /// <param name="rule">specific validation rule which should be checked</param>
        /// <returns>true if validation passed, false otherwise</returns>
        bool validate(AFField field, StringBuilder errorMsgs, AFValidationRule rule);
    }
}
