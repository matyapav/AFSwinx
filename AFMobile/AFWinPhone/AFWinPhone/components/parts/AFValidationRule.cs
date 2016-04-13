using System;
using AFWinPhone.enums;

namespace AFWinPhone.components.parts
{
    public class AFValidationRule
    {
        private SupportedValidations validationType;
        private String value;

        public AFValidationRule()
        {
        }

        public SupportedValidations getValidationType()
        {
            return this.validationType;
        }

        public void setValidationType(SupportedValidations validationType)
        {
            this.validationType = validationType;
        }

        public String getValue()
        {
            return this.value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }
    }
}
