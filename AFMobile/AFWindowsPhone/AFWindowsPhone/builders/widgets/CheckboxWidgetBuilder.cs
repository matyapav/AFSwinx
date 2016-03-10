﻿using AFWindowsPhone.builders.components.parts;
using AFWindowsPhone.builders.skins;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media;

namespace AFWindowsPhone.builders.widgets
{
    class CheckboxWidgetBuilder : BasicBuilder
    {
        public CheckboxWidgetBuilder(Skin skin, FieldInfo properties) : base(skin, properties)
        {
        }

        public override FrameworkElement buildFieldView()
        {
            CheckBox checkBox = new CheckBox();
            checkBox.Foreground = new SolidColorBrush(getSkin().getFieldColor());
            //checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (getProperties().isReadOnly())
            {
                checkBox.IsEnabled = false;
            }
            return checkBox;
        }

        public override void setData(AFField field, Object value)
        {
            CheckBox box = (CheckBox)field.getFieldView();
            box.IsChecked = Convert.ToBoolean(value.ToString());
            field.setActualData(value);
        }

        public override Object getData(AFField field)
        {
            CheckBox box = (CheckBox)field.getFieldView();
            return box.IsChecked.ToString();
        }
    }
}
