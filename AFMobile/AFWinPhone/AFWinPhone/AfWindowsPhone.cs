using AFWinPhone.builders;
using AFWinPhone.components.types;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using AFWinPhone.builders.skins;

namespace AFWinPhone
{
    public class AfWindowsPhone
    {
        private static AfWindowsPhone instance = null;
        private Dictionary<String, AFComponent> createdComponents;
        private Skin defaultSkin;

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

        public AFComponent getCreatedComponentByName(String name)
        {
            if (createdComponents.ContainsKey(name))
            {
                return createdComponents[name];
            }
            else
            {
                return null;
            }
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

        public void removeCreatedComponent(String name)
        {
            if (createdComponents.Remove(name))
            {
                Debug.WriteLine("Component "+name+" was removed.");
            }
            else
            {
                Debug.WriteLine("Component "+ name +" is not in created components");
            }
        }

        public void removeAllComponents()
        {
            createdComponents.Clear();
        }

        public FormBuilder getFormBuilder()
        {
            return new FormBuilder();
        }

        public ListBuilder getListBuilder()
        {
            return new ListBuilder();
        }

        public Skin getDefaultSkin()
        {
            return defaultSkin;
        }

        public void setDefaultSkin(Skin defaultSkin)
        {
            this.defaultSkin = defaultSkin;
        }

    }
}
