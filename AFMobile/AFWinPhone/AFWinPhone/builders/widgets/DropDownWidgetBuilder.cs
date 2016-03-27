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

        public DropDownWidgetBuilder(Skin skin, AFFieldInfo properties) : base(skin, properties)
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
            if (getProperties().isReadOnly())
            {
                comboBox.IsEnabled = false;
            }
            return comboBox;
        }

        public override object getData(AFField field)
        {
            ComboBox comboBox = (ComboBox)field.getFieldView();
            comboBox.FontSize = getSkin().getFieldFontSize();
            if (field.getFieldInfo().getOptions() != null)
            {
                foreach (AFOptions option in field.getFieldInfo().getOptions())
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

        public override void setData(AFField field, object value)
        {
            ComboBox comboBox = (ComboBox)field.getFieldView();
            if (value == null)
            {
                comboBox.SelectedIndex = 0;
                field.setActualData(comboBox.SelectedItem);
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
            if (field.getFieldInfo().getOptions() != null)
            {
                foreach (AFOptions option in field.getFieldInfo().getOptions())
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
                    field.setActualData(comboBox.SelectedItem);
                    return;
                }
            }
            field.setActualData(value);
        }

        private List<String> convertOptionsIntoList()
        {
            List<String> list = new List<String>();
            if (getProperties().getOptions() != null)
            {
                foreach (AFOptions option in getProperties().getOptions())
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
