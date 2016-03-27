using System;

namespace AFWinPhone.components.parts
{
    public class AFOptions
    {
        private String key;
        private String value;

        public AFOptions()
        {
        }

        public String getKey()
        {
            return this.key;
        }

        public void setKey(String key)
        {
            this.key = key;
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
