using System;
using AFWinPhone.components.parts;
using System.Diagnostics;
using AFWinPhone.enums;
using AFWinPhone.utils;
using Windows.Data.Json;

namespace AFWinPhone.parsers
{
    public class JSONDefinitionParser : JSONParser
    {
        public AFClassInfo parse(String classInfoStr, bool parsingInnerClass)
        {
            AFClassInfo definition = null;

            try
            {
                JsonObject classInfo = JsonObject.Parse(classInfoStr);
                if (!parsingInnerClass)
                {
                    classInfo = (JsonObject) Utils.TryToGetValueFromJson(classInfo[(Constants.CLASS_INFO)]);
                }

                //Parse class name and create data pack with this class name
                Debug.WriteLine("PARSING CLASS " + Utils.TryToGetValueFromJson(classInfo[Constants.CLASS_NAME]));
                definition = new AFClassInfo((String) Utils.TryToGetValueFromJson(classInfo[Constants.CLASS_NAME]));

                //Parse layout
                JsonObject layout = (JsonObject) Utils.TryToGetValueFromJson(classInfo[Constants.LAYOUT]);
                definition.setLayout(createTopLayoutProperties(layout));

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
                        definition.addInnerClass(parse(innerClass.Stringify(), true)); //recursion;
                    }
                }
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.StackTrace);
            }

            return definition;
        }

        private AFFieldInfo parseFieldInfo(JsonObject field)
        {
            Debug.WriteLine("PARSING FIELD " + Utils.TryToGetValueFromJson(field[Constants.ID]));
            AFFieldInfo fieldInfo = new AFFieldInfo();
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
            //add number rule - it is not among others in json 
            try
            {
                if (fieldInfo.getWidgetType().Equals(SupportedWidgets.NUMBERFIELD) || fieldInfo.getWidgetType().Equals(SupportedWidgets.NUMBERDOUBLEFIELD))
                {
                    AFValidationRule numberRule = new AFValidationRule();
                    numberRule.setValidationType(SupportedValidations.NUMBER);
                    numberRule.setValue("");
                    fieldInfo.addRule(numberRule);
                }
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.Message);
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

        private TopLevelLayout createTopLayoutProperties(JsonObject layoutJson)
        {
            TopLevelLayout layoutProp = new TopLevelLayout();
            if (layoutJson == null)
            {
                layoutProp.setLayoutDefinition(LayoutDefinitions.ONECOLUMNLAYOUT);
                layoutProp.setLayoutOrientation(LayoutOrientation.AXISX);
                return layoutProp; //with default values
            }

            try
            {
                String layDefName = (String)Utils.TryToGetValueFromJson(layoutJson[Constants.LAYOUT_DEF]);
                LayoutDefinitions layDef = Utils.ValueOf<LayoutDefinitions>(typeof(LayoutDefinitions), layDefName);
                if (layDef != null)
                {
                    layoutProp.setLayoutDefinition(layDef);
                }

                String orientation = (String)Utils.TryToGetValueFromJson(layoutJson[Constants.LAYOUT_ORIENT]);
                LayoutOrientation layOrient = Utils.ValueOf<LayoutOrientation>(typeof(LayoutOrientation), orientation);
                if (layOrient != null)
                {
                    layoutProp.setLayoutOrientation(layOrient);
                }
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.Message);
                //e.printStackTrace();
            }
            return layoutProp;
        }



        private Layout createLayoutProperties(JsonObject layoutJson)
        {
            Layout layoutProp = new Layout();
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

        private AFValidationRule createRule(JsonObject ruleJson)
        {
            AFValidationRule rule = new AFValidationRule();
            rule.setValidationType(Utils.ValueOf<SupportedValidations>(typeof(SupportedValidations), (String) Utils.TryToGetValueFromJson(ruleJson[Constants.VALIDATION_TYPE])));
            rule.setValue((String) Utils.TryToGetValueFromJson(ruleJson[Constants.VALUE]));
            return rule;
        }

        private AFOptions createOption(JsonObject optionJson)
        {
            AFOptions option = new AFOptions();
            option.setKey((String) Utils.TryToGetValueFromJson(optionJson[Constants.KEY]));
            option.setValue((String) Utils.TryToGetValueFromJson(optionJson[Constants.VALUE]));
            return option;
        }

    }
}
