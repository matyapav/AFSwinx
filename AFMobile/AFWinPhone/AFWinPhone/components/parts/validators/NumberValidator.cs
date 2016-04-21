using System;
using System.Diagnostics;
using System.Globalization;
using System.Text;
using Windows.UI.Xaml.Controls;
using AFWinPhone.enums;
using AFWinPhone.utils;

namespace AFWinPhone.components.parts.validators
{
    /// <summary>
    /// Validates if inserted value in field is number.
    /// </summary>
    class NumberValidator : AFValidator
    {
        public bool validate(AFField field, StringBuilder errorMsgs, AFValidationRule rule)
        {
            bool validationIsFine = true;
            String message = "";
            TextBox numberField = (TextBox)field.getFieldView();
            if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.NUMBERFIELD))
            {
                try
                {
                    if (!String.IsNullOrWhiteSpace(numberField.Text))
                    {
                        Convert.ToInt32(numberField.Text);
                    }

                }
                catch (Exception ex)
                {
                    validationIsFine = false;
                    Debug.WriteLine(ex.StackTrace);
                    message = Localization.translate("validation.integer");
                }
            }
            //for doubles 
            if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.NUMBERDOUBLEFIELD))
            {
                try
                {
                    if (!String.IsNullOrWhiteSpace(numberField.Text))
                    {

                        double.Parse(numberField.Text.Replace(',','.'),NumberStyles.AllowDecimalPoint, CultureInfo.InvariantCulture);
                    }

                }
                catch (Exception ex)
                {
                    validationIsFine = false;
                    Debug.WriteLine(ex.StackTrace);
                    message = Localization.translate("validation.double");
                }
            }
            if (!validationIsFine)
            {
                errorMsgs.Append(message);
            }
            return validationIsFine;
        }
    }
}
