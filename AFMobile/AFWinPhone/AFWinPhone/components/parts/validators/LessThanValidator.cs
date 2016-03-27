
using AFWinPhone.enums;
using AFWinPhone.utils;
using System;
using System.Text;
using AFWinPhone.components.types;

namespace AFWinPhone.components.parts.validators
{
    class LessThanValidator : AFValidator
    {
        public bool validate(AFField field, StringBuilder errorMsgs, AFValidationRule rule)
        {
            bool validationIsFine = true;
            Object otherData = ((AFForm)field.getParent()).getDataFromFieldWithId(rule.getValue());
            if (otherData != null)
            {
                if (Utils.IsFieldNumberField(field))
                {
                    //TODO pro cisla
                }
                if (field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.CALENDAR))
                {
                    Object fieldData = ((AFForm)field.getParent()).getDataFromFieldWithId(field.getId());
                    if (fieldData != null)
                    {
                        DateTime? date = Utils.ParseDate(fieldData.ToString());
                        DateTime? otherDate = Utils.ParseDate(otherData.ToString());
                        if (date > otherDate)
                        {
                            validationIsFine = false;
                        }
                    }
                }
                if (!validationIsFine)
                {
                    String otherFieldLabelText = (field.getParent()).getFieldById(rule.getValue()).getFieldInfo().getLabelText();
                    errorMsgs.Append(Localization.translate("validation.lessthan") + " "
                            + Localization.translate(otherFieldLabelText));
                }
            }
            return validationIsFine;

        }
    }
}
