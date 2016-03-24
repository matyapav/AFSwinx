using AFWinPhone.builders;
using AFWinPhone.components.types;
using System;
using System.Collections.Generic;

namespace AFWinPhone
{
    public class AfWindowsPhone
    {
        private static AfWindowsPhone instance = null;
        private Dictionary<String, AFComponent> createdComponents;

        private AfWindowsPhone()
        {
            createdComponents = new Dictionary<String, AFComponent>();
        }

        public static AfWindowsPhone getInstance()
        {
            if (instance == null)
            {
                instance = new AfWindowsPhone();
            }
            return instance;
        }

        public Dictionary<String, AFComponent> getCreatedComponents()
        {
            return createdComponents;
        }

        public void addCreatedComponent(String name, AFComponent component)
        {
            if (createdComponents.ContainsKey(name))
            {
                createdComponents[name] = component;
            }
            else
            { 
                createdComponents.Add(name, component);
            }
        }

        public FormBuilder getFormBuilder()
        {
            return new FormBuilder();
        }

        public ListBuilder getListBuilder()
        {
            return new ListBuilder();
        }

    }
}
