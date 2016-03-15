﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using AFWindowsPhone.builders.components.parts;
using System.Diagnostics;
using AFWindowsPhone.enums;
using AFWindowsPhone.utils;
using Windows.Data.Json;

namespace AFWindowsPhone.parsers
{
    class JSONDefinitionParser : JSONParser
    {
        public ClassDefinition parse(JsonObject classInfo)
        {
            ClassDefinition definition = null;

            try
            {
                //Parse class name and create data pack with this class name
                Debug.WriteLine("PARSING CLASS " + classInfo[Constants.CLASS_NAME]);
                definition = new ClassDefinition((String) Utils.TryToGetValueFromJson(classInfo[Constants.CLASS_NAME]));

                //Parse layout
                JsonObject layout = (JsonObject) Utils.TryToGetValueFromJson(classInfo[Constants.LAYOUT]);
                definition.setLayout(createLayoutProperties(layout));

                //Parse fields
                JsonArray fields = (JsonArray) Utils.TryToGetValueFromJson(classInfo[Constants.FIELD_INFO]);
                if (fields != null)
                {
                    for (int i = 0; i < fields.Count; i++)
                    {
                        JsonObject field = fields[i].GetObject();
                        definition.addFieldInfo(parseFieldInfo(field));
                    }
                }
                JsonArray innerClasses = (JsonArray) Utils.TryToGetValueFromJson(classInfo[Constants.INNER_CLASSES]);
                if (innerClasses != null)
                {
                    for (int i = 0; i < innerClasses.Count; i++)
                    {
                        JsonObject innerClass = (JsonObject) Utils.TryToGetValueFromJson(innerClasses[i]);
                        definition.addInnerClass(parse(innerClass)); //recursion;
                    }
                }
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.StackTrace);
            }

            return definition;
        }

        private FieldInfo parseFieldInfo(JsonObject field)
        {
            Debug.WriteLine("PARSING FIELD " + Utils.TryToGetValueFromJson(field[Constants.ID]));
            FieldInfo fieldInfo = new FieldInfo();
            try
            {
                fieldInfo.setWidgetType(Utils.ValueOf<SupportedWidgets>(typeof(SupportedWidgets), (String) Utils.TryToGetValueFromJson(field[Constants.WIDGET_TYPE])));
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.Message);
            }
            fieldInfo.setId((String) Utils.TryToGetValueFromJson(field[Constants.ID]));

            fieldInfo.setLabelText((String) Utils.TryToGetValueFromJson(field[Constants.LABEL]));
            fieldInfo.setIsClass((Boolean) Utils.TryToGetValueFromJson(field[Constants.CLASS_TYPE]));
            fieldInfo.setVisible((Boolean) Utils.TryToGetValueFromJson(field[Constants.VISIBLE]));
            fieldInfo.setReadOnly((Boolean) Utils.TryToGetValueFromJson(field[Constants.READ_ONLY]));
            //field layout
            fieldInfo.setLayout(createLayoutProperties((JsonObject) Utils.TryToGetValueFromJson(field[Constants.LAYOUT])));
            //rules)
            JsonArray rules = (JsonArray) Utils.TryToGetValueFromJson(field[Constants.RULES]);
            if (rules != null)
            {
                for (int i = 0; i < rules.Count; i++)
                {
                    fieldInfo.addRule(createRule((JsonObject) Utils.TryToGetValueFromJson(rules[i])));
                }
            }
            //options
            JsonArray options = (JsonArray) Utils.TryToGetValueFromJson(field[Constants.OPTIONS]);
            if (options != null)
            {
                for (int i = 0; i < options.Count; i++)
                {
                    fieldInfo.addOption(createOption((JsonObject) Utils.TryToGetValueFromJson(options[i])));
                }
            }

            return fieldInfo;
        }


        private LayoutProperties createLayoutProperties(JsonObject layoutJson)
        {
            LayoutProperties layoutProp = new LayoutProperties();
            if (layoutJson == null)
            {
                layoutProp.setLayoutDefinition(LayoutDefinitions.ONECOLUMNLAYOUT);
                layoutProp.setLayoutOrientation(LayoutOrientation.AXISX);
                layoutProp.setLabelPosition(LabelPosition.BEFORE);
                return layoutProp; //with default values
            }

            try
            {
                String layDefName = (String) Utils.TryToGetValueFromJson(layoutJson[Constants.LAYOUT_DEF]);
                LayoutDefinitions layDef = Utils.ValueOf<LayoutDefinitions>(typeof(LayoutDefinitions),layDefName);
                if (layDef != null)
                {
                    layoutProp.setLayoutDefinition(layDef);
                }

                String orientation = (String) Utils.TryToGetValueFromJson(layoutJson[Constants.LAYOUT_ORIENT]);
                LayoutOrientation layOrient = Utils.ValueOf<LayoutOrientation>(typeof(LayoutOrientation),orientation);
                if (layOrient != null)
                {
                    layoutProp.setLayoutOrientation(layOrient);
                }

                String position = (String) Utils.TryToGetValueFromJson(layoutJson[Constants.LABEL_POS]);

                LabelPosition labelPos = Utils.ValueOf<LabelPosition>(typeof(LabelPosition),position);
                if (labelPos != null)
                {
                    layoutProp.setLabelPosition(labelPos);
                }
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.Message);
                //e.printStackTrace();
            }
            return layoutProp;
        }

        private ValidationRule createRule(JsonObject ruleJson)
        {
            ValidationRule rule = new ValidationRule();
            rule.setValidationType((String) Utils.TryToGetValueFromJson(ruleJson[Constants.VALIDATION_TYPE]));
            rule.setValue((String) Utils.TryToGetValueFromJson(ruleJson[Constants.VALUE]));
            return rule;
        }

        private FieldOption createOption(JsonObject optionJson)
        {
            FieldOption option = new FieldOption();
            option.setKey((String) Utils.TryToGetValueFromJson(optionJson[Constants.KEY]));
            option.setValue((String) Utils.TryToGetValueFromJson(optionJson[Constants.VALUE]));
            return option;
        }

    }
}
