using System;

namespace AFWinPhone.components.parts
{
    public class AFValidationRule
    {
        private String validationType;
        private String value;

        public AFValidationRule()
        {
        }

        public String getValidationType()
        {
            return this.validationType;
        }

        public void setValidationType(String validationType)
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
