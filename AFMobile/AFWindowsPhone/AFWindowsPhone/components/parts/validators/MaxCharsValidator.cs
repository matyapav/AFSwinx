﻿using AFWindowsPhone.enums;
using AFWindowsPhone.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI.Xaml.Controls;

namespace AFWindowsPhone.builders.components.parts.validators
{
    class MaxCharsValidator : AFValidator
    {
        public bool validate(AFField field, StringBuilder errorMsgs, ValidationRule rule)
        {
            bool validationIsFine = true;
            if (Utils.IsFieldWritable(field.getFieldInfo().getWidgetType()))
            {
                TextBox textfield = (TextBox)field.getFieldView();
                if (textfield.Text != null && textfield.Text.Length > Convert.ToInt32(rule.getValue()))
                {
                    validationIsFine = false;
                }
            }
            if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.PASSWORD))
            {
                PasswordBox textfield = (PasswordBox)field.getFieldView();
                if (textfield.Password != null && textfield.Password.Length > Convert.ToInt32(rule.getValue()))
                {
                    validationIsFine = false;
                }
            }
            if (!validationIsFine)
            {
                errorMsgs.Append(Localization.translate("validation.maxchars"));
            }
            return validationIsFine;
        }
    }
}