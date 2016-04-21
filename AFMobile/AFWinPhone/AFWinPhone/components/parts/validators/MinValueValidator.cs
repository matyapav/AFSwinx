using AFWinPhone.utils;
using System;
using System.Diagnostics;
using System.Text;
using Windows.UI.Xaml.Controls;

namespace AFWinPhone.components.parts.validators
{
    /// <summary>
    /// Validates MIN rule, which checks if value in field is more or equal than value specified in rule.
    /// </summary>
    class MinValueValidator : AFValidator
    {
        public bool validate(AFField field, StringBuilder errorMsgs, AFValidationRule rule)
        {
            bool validationIsFine = true;
            if (Utils.IsFieldNumberField(field))
            {
                TextBox numberField = (TextBox) field.getFieldView();
                try
                {
                    if (String.IsNullOrEmpty(numberField.Text) &&
                        Convert.ToDouble(numberField.Text) < Convert.ToDouble(rule.getValue()))
                    {
                        validationIsFine = false;
                    }
                }
                catch (Exception ex) //catch convert exception
                {
                    validationIsFine = true; //if there is no number in field we cant validate its value
                    Debug.WriteLine(ex.StackTrace);
                }
            }
            if (!validationIsFine)
            {
                errorMsgs.Append(Localization.translate("validation.minval") + " " + rule.getValue());
            }
            return validationIsFine;
        }
    }
}
