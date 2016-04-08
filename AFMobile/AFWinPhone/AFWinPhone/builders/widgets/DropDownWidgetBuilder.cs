using System;
using System.Collections.Generic;
using AFWinPhone.components.parts;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using AFWinPhone.builders.skins;
using AFWinPhone.utils;

namespace AFWinPhone.builders.widgets
{
    class DropDownWidgetBuilder : BasicBuilder
    {

        public DropDownWidgetBuilder(Skin skin, AFField field) : base(skin, field)
        {
        }

        public override FrameworkElement buildFieldView()
        {
            ComboBox comboBox = new ComboBox();
            comboBox.FontSize = getSkin().getFieldFontSize();
            comboBox.FontFamily = getSkin().getFieldFont();
            //note - do not set color - should be according to wp theme

            List<String> optionsList = convertOptionsIntoList();
            if (optionsList != null)
            {
                comboBox.ItemsSource = optionsList;
            }
            if (getField().getFieldInfo().isReadOnly())
            {
                comboBox.IsEnabled = false;
            }
            return comboBox;
        }

        public override object getData()
        {
            ComboBox comboBox = (ComboBox)getField().getFieldView();
            comboBox.FontSize = getSkin().getFieldFontSize();
            if (getField().getFieldInfo().getOptions() != null)
            {
                foreach (AFOptions option in getField().getFieldInfo().getOptions())
                {
                    if (Localization.translate(option.getValue()).Equals(comboBox.SelectedItem.ToString()))
                    {
                        return option.getKey();
                    }
                }
            }
            if (comboBox.SelectedItem.ToString().Equals(Localization.translate("option.yes")))
            {
                return "true";
            }
            else if (comboBox.SelectedItem.ToString().Equals(Localization.translate("option.no")))
            {
                return "false";
            }
            else {
                return comboBox.SelectedItem.ToString();
            }
        }

        public override void setData(object value)
        {
            ComboBox comboBox = (ComboBox)getField().getFieldView();
            if (value == null)
            {
                comboBox.SelectedIndex = 0;
                getField().setActualData(comboBox.SelectedItem);
                return;
            }
            if (value.ToString().ToLower().Equals("true"))
            {
                value = Localization.translate("option.yes");
            }
            else if (value.ToString().ToLower().Equals("false"))
            {
                value = Localization.translate("option.no");
            }
            if (getField().getFieldInfo().getOptions() != null)
            {
                foreach (AFOptions option in getField().getFieldInfo().getOptions())
                {
                    if (option.getKey().Equals(value))
                    {
                        value = Localization.translate(option.getValue());
                        break;
                    }
                }
            }
            for (int i = 0; i < comboBox.Items.Count; i++)
            {
                if (comboBox.Items[i].ToString().Equals(value))
                {
                    comboBox.SelectedIndex = i;
                    getField().setActualData(comboBox.SelectedItem);
                    return;
                }
            }
            getField().setActualData(value);
        }

        private List<String> convertOptionsIntoList()
        {
            List<String> list = new List<String>();
            if (getField().getFieldInfo().getOptions() != null)
            {
                foreach (AFOptions option in getField().getFieldInfo().getOptions())
                {
                    if (option.getValue().Equals("true"))
                    {
                        list.Add(Localization.translate("option.yes"));
                    }
                    else if (option.getValue().Equals("false"))
                    {
                        list.Add(Localization.translate("option.no"));
                    }
                    else {
                        list.Add(Localization.translate(option.getValue()));
                    }
                }
                return list;
            }
            return null;
        }
    }
}
