﻿using AFWinPhone.components.parts;
using AFWinPhone.builders.skins;
using AFWinPhone.utils;
using System;
using System.Globalization;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media;

namespace AFWinPhone.builders.widgets
{
    /// <summary>
    /// This class is responsible for building datepicker component.
    /// </summary>
    class DateWidgetBuilder : BasicBuilder
    {
        private String dateFormat;

        public DateWidgetBuilder(Skin skin, AFField field) : base(skin, field)
        {
            this.dateFormat = "dd.MM.yyyy"; //Default date format
        }


        public override FrameworkElement buildFieldView()
        {
            StackPanel dateLayout = new StackPanel();
            dateLayout.Orientation = Orientation.Horizontal;

            DatePicker datePicker = new DatePicker();
            datePicker.VerticalAlignment = VerticalAlignment.Top;
            datePicker.HorizontalAlignment = HorizontalAlignment.Stretch;

            datePicker.FontFamily = getSkin().getFieldFont();
            datePicker.FontSize = getSkin().getFieldFontSize();
            //note .. do not set color - should be according to wp theme

            if (getField().getFieldInfo().isReadOnly())
            {
                datePicker.IsEnabled = false;
                datePicker.Foreground = new SolidColorBrush(Colors.LightGray);
            }
            return datePicker;
        }

        public override void setData(Object value)
        {
            DatePicker dateText = (DatePicker)getField().getFieldView();
            DateTime? date = Utils.ParseDate(value.ToString());
            if (date != null)
            {
                dateText.Date = date.Value;
                getField().setActualData(date.Value.ToString("dd.MM.yyyy", CultureInfo.InvariantCulture));
            }
            else {
                //parsing totally failed maybe exception
            }
        }

        public override Object getData()
        {
            DatePicker dateText = (DatePicker)getField().getFieldView();
            DateTimeOffset? date = dateText.Date;
            if (date != null)
            {
                return date.Value.ToString("yyyy-MM-dd'T'HH:mm:ss", CultureInfo.InvariantCulture);
            }
            return null;
        }
    }
}
