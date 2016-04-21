using AFWinPhone.enums;
using AFWinPhone.utils;
using System;
using System.Text;
using Windows.UI.Xaml.Controls;

namespace AFWinPhone.components.parts.validators
{
    /// <summary>
    /// Validates REQUIRED rule, which checks field is filled.
    /// </summary>
    class RequiredValidator : AFValidator
    {
        public bool validate(AFField field, StringBuilder errorMsgs, AFValidationRule rule)
        {
            bool validationIsFine = true;
            if (Utils.IsFieldWritable(field.getFieldInfo().getWidgetType()))
            {
                TextBox textfield = (TextBox) field.getFieldView();
                if (String.IsNullOrWhiteSpace(textfield.Text))
                {
                    validationIsFine = false;
                }
            }
            if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.PASSWORD))
            {
                PasswordBox passwordField = (PasswordBox)field.getFieldView();
                if (String.IsNullOrWhiteSpace(passwordField.Password))
                {
                    validationIsFine = false;
                }
            }
            if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.OPTION))
            {
                StackPanel radioGroup = (StackPanel)field.getFieldView();
                bool atLeastOneIsChecked = false;
                foreach(RadioButton child in radioGroup.Children)
                {
                    if(child.IsChecked.Value)
                    {
                        atLeastOneIsChecked = true;
                    }
                }
                if (!atLeastOneIsChecked)
                {
                    validationIsFine = false;
                }
            }
            if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.DROPDOWNMENU))
            {
                ComboBox cb = (ComboBox)field.getFieldView();
                if (cb.SelectedItem == null)
                {
                    validationIsFine = false;
                }
            }
            if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.CALENDAR))
            {
                DatePicker textfield = (DatePicker)field.getFieldView();
                if (String.IsNullOrWhiteSpace(textfield.Date.ToString()))
                {
                    validationIsFine = false;
                }
            }
            if (!validationIsFine)
            {
                errorMsgs.Append(Localization.translate("validation.required"));
            }
            return validationIsFine;
        }
    }
}
