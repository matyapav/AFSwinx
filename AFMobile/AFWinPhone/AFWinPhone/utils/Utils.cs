using AFWinPhone.components.parts;
using AFWinPhone.components.types;
using AFWinPhone.enums;
using AFWinPhone.rest.connection;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Reflection;
using System.Text;
using System.Xml.Linq;
using Windows.Data.Json;
using Windows.Data.Xml.Dom;

namespace AFWinPhone.utils
{
    public class Utils
    {

        public static bool IsFieldWritable(SupportedWidgets widgetType)
        {
            return widgetType.Equals(SupportedWidgets.TEXTFIELD) || widgetType.Equals(SupportedWidgets.NUMBERFIELD)
                    || widgetType.Equals(SupportedWidgets.NUMBERDOUBLEFIELD);
        }

        public static bool IsFieldNumberField(AFField field)
        {
            return field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.NUMBERFIELD)
                    || field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.NUMBERDOUBLEFIELD);
        }

        public static String GetConnectionEndPoint(AFSwinxConnection connection)
        {
            StringBuilder endPointBuilder = new StringBuilder();
            if (!String.IsNullOrEmpty(connection.getProtocol()))
            {
                endPointBuilder.Append(connection.getProtocol());
                endPointBuilder.Append("://");
            }
            if (!String.IsNullOrEmpty(connection.getAddress()))
            {
                endPointBuilder.Append(connection.getAddress());
            }
            if (connection.getPort() != 0)
            {
                endPointBuilder.Append(":");
                endPointBuilder.Append(connection.getPort());
            }
            if (!String.IsNullOrEmpty(connection.getParameters()))
            {
                endPointBuilder.Append(connection.getParameters());
            }
            return endPointBuilder.ToString();
        }

        public static bool ShouldBeInvisible(String column, AFComponent component)
        {
            foreach (AFField field in component.getFields())
            {
                if (field.getId().Equals(column))
                {
                    return !field.getFieldInfo().isVisible();
                }
            }
            return true;
        }

        public static DateTime? ParseDate(String date)
        {
            
            String[] formats = { "Default", "dd.MM.yyyy" };
            if (date != null)
            {
                foreach (String format in formats)
                {
                    try
                    {
                        if (format.Equals("Default"))
                        {
                            return DateTime.Parse(date);
                        }
                        return DateTime.ParseExact(date, format, CultureInfo.InvariantCulture);
                    }
                    catch (FormatException e)
                    {
                        Debug.WriteLine("Cannot parse date " + date + " using format " + format);
                        Debug.WriteLine(e.StackTrace);
                    }
                }
            }
            return null;
        }

        public  static XmlDocument BuildDocumentFromFile(String pathToFile)
        {
            XmlDocument doc = new XmlDocument();
            try { 
            doc.LoadXml(XDocument.Load(pathToFile).ToString());
            }
            catch (Exception e)
            {
                Debug.WriteLine("File not found");
                Debug.WriteLine(e.StackTrace);
            }
            return doc;
        }

        public static T ValueOf<T>(Type clazz, String value)
        {
            foreach (System.Reflection.FieldInfo field in clazz.GetRuntimeFields())
            {
                try
                {
                    T enumeration = (T) field.GetValue(null);
                    foreach (System.Reflection.FieldInfo enumField in enumeration.GetType().GetRuntimeFields())
                    {
                        if (enumField.GetValue(enumeration).Equals(value))
                        {
                            return enumeration;
                        }

                    }
                }
                catch (Exception e)
                {
                    Debug.WriteLine(e.Message); //do nothing only inform about it
                }

            }
            return default(T); //not found in enum

        }

        public static Object TryToGetValueFromJson(IJsonValue value)
        {
            switch (value.ValueType)
            {
                case JsonValueType.Null:
                    return null;
                case JsonValueType.String:
                    return value.GetString();
                case JsonValueType.Boolean:
                    return value.GetBoolean();
                case JsonValueType.Object:
                    return value.GetObject();
                case JsonValueType.Number:
                    return value.GetNumber();
                case JsonValueType.Array:
                    return value.GetArray();
                default:
                    return null;
            }
        }

        public static bool TryToConvertIntoBoolean(String value)
        {
            try
            {
                return Convert.ToBoolean(value);
            }
            catch (Exception)
            {
                return false;
            }
        }

        public static String evaluateElExpression(String expressionToEvaluate,
                                            Dictionary<String, String> parameters)
        {
            // To chaining string use string builder
            StringBuilder replacedValue = new StringBuilder();
            // Split expression by #{ it gives you strings between and after value which should be
            // replaced
            String[] values = expressionToEvaluate.Split(new[] { "#{" }, StringSplitOptions.None);
            bool firstCycle = true;
            foreach (String value in values)
            {
                // in first cycle add everything because split is done by #{ which means that before
                // first char sequence #{ is plain text
                if (firstCycle)
                {
                    replacedValue.Append(value.Substring(0, value.Length));
                    firstCycle = false;
                    continue;
                }
                // This are values behind string which will be replaced. in first position is string to
                // replaced
                String[] valuesBehind = value.Split(new[] { "}" }, StringSplitOptions.None);
                // Find replaced value and append it
                String key = valuesBehind[0].Substring(0, valuesBehind[0].Length);
                String elValue = parameters.ContainsKey(key)? parameters[key] : "null";
                replacedValue.Append(elValue);
                // If some values left - this means that there is more } brackets ex: #{value}/a}/a}
                // then append them too
                for (int i = 1; i < valuesBehind.Length; i++)
                {
                    if (String.IsNullOrEmpty(valuesBehind[i]))
                    {
                        continue;
                    }
                    replacedValue.Append(valuesBehind[i]);
                    char firstChar = valuesBehind[i][0];
                    //Because split was done by } then it should not be there, then add it if left brackets
                    if (firstChar == '{')
                    {
                        replacedValue.Append("}");
                    }

                }
            }
            return replacedValue.ToString();
        }
    }
}
