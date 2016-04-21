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
    /// <summary>
    /// Util methods used within the application.
    /// </summary>
    public class Utils
    {
        /// <summary>
        /// Determines if field is writable based on widget type, that means user can write into it.
        /// </summary>
        /// <param name="widgetType">widget type of field</param>
        /// <returns>true if the field is writable, false otherwise</returns>
        public static bool IsFieldWritable(SupportedWidgets widgetType)
        {
            return widgetType.Equals(SupportedWidgets.TEXTFIELD) || widgetType.Equals(SupportedWidgets.NUMBERFIELD)
                    || widgetType.Equals(SupportedWidgets.NUMBERDOUBLEFIELD);
        }

        /// <summary>
        /// Determine if field is number field, that means user can only write numbers in it.
        /// </summary>
        /// <param name="field">field to check</param>
        /// <returns>true if field is number field, false otherwise</returns>
        public static bool IsFieldNumberField(AFField field)
        {
            return field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.NUMBERFIELD)
                    || field.getFieldInfo().getWidgetType().Equals(SupportedWidgets.NUMBERDOUBLEFIELD);
        }

        /// <summary>
        /// Gets connection end point URL made from parts saved in given connection.
        /// </summary>
        /// <param name="connection">holds all parts of end point</param>
        /// <returns>string representing connection end point</returns>
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

        /// <summary>
        /// Determines if column in list should be invisible or not.
        /// </summary>
        /// <param name="column">specified column to check</param>
        /// <param name="component">component to which should be column added</param>
        /// <returns>true, if should be invisible, false otherwise</returns>
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

        /// <summary>
        /// Parses string into date using ISO format or dd.MM.yyyy format.
        /// </summary>
        /// <param name="date">string representation of date</param>
        /// <returns>parsed date if it was in one of formats, null if date is not in specified formats.</returns>
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

        /// <summary>
        /// Builds Xml document from specified file.
        /// </summary>
        /// <param name="pathToFile">path to file which should be document builded from</param>
        /// <returns>built xml document</returns>
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

        /// <summary>
        /// Returns enumeration based on its value.
        /// </summary>
        /// <typeparam name="T">type of enumeration</typeparam>
        /// <param name="clazz">class where should program look for</param>
        /// <param name="value">value of enumeration</param>
        /// <returns>Specific enumeration if found</returns>
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

        /// <summary>
        /// Tries to get value from Json file. Chooses suitable method for each value type that might show up in Json file.
        /// </summary>
        /// <param name="value">value which should be got</param>
        /// <returns>value in specific value type</returns>
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

        /// <summary>
        /// Converts String value into boolean
        /// </summary>
        /// <param name="value">value to be converted</param>
        /// <returns>converted value, false if convertion failed</returns>
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
