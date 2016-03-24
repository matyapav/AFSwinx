package cz.cvut.fel.matyapav.afandroid.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.cvut.fel.matyapav.afandroid.components.parts.ClassDefinition;
import cz.cvut.fel.matyapav.afandroid.components.parts.FieldInfo;
import cz.cvut.fel.matyapav.afandroid.components.parts.FieldOption;
import cz.cvut.fel.matyapav.afandroid.components.parts.LayoutProperties;
import cz.cvut.fel.matyapav.afandroid.components.parts.ValidationRule;
import cz.cvut.fel.matyapav.afandroid.enums.LabelPosition;
import cz.cvut.fel.matyapav.afandroid.enums.LayoutDefinitions;
import cz.cvut.fel.matyapav.afandroid.enums.LayoutOrientation;
import cz.cvut.fel.matyapav.afandroid.enums.SupportedWidgets;
import cz.cvut.fel.matyapav.afandroid.utils.Constants;

/**
 * Created by Pavel on 17.12.2015.
 */
public class JSONDefinitionParser implements JSONParser {

    @Override
    public ClassDefinition parse(String classInfoJson, boolean parsingInnerClass){

        ClassDefinition definition = null;

        try {
            JSONObject classInfo = new JSONObject(classInfoJson);
            if(!parsingInnerClass){
                classInfo = classInfo.optJSONObject(Constants.CLASS_INFO);
            }
            //Parse class name and create data pack with this class name
            System.err.println("PARSING CLASS "+ classInfo.getString(Constants.CLASS_NAME));
            definition = new ClassDefinition(classInfo.getString(Constants.CLASS_NAME));

            //Parse layout
            JSONObject layout = classInfo.optJSONObject(Constants.LAYOUT);
            definition.setLayout(createLayoutProperties(layout));

            //Parse fields
            JSONArray fields = classInfo.optJSONArray(Constants.FIELD_INFO);
            if(fields != null){
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject field = fields.getJSONObject(i);
                    definition.addFieldInfo(parseFieldInfo(field));
                }
            }
            JSONArray innerClasses = classInfo.optJSONArray(Constants.INNER_CLASSES);
            if(innerClasses != null){
                for (int i = 0; i < innerClasses.length(); i++) {
                    JSONObject innerClass = innerClasses.getJSONObject(i);
                    definition.addInnerClass(parse(innerClass.toString(), true)); //recursion;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return definition;
    }

    private FieldInfo parseFieldInfo(JSONObject field) throws JSONException {
        System.out.println("PARSING FIELD " + field.getString(Constants.ID));
        FieldInfo fieldInfo = new FieldInfo();
        try {
            fieldInfo.setWidgetType(SupportedWidgets.valueOf(field.optString(Constants.WIDGET_TYPE)));
        }catch (IllegalArgumentException e){
            System.err.println(e.getLocalizedMessage());
        }
        fieldInfo.setId(field.getString(Constants.ID));

        fieldInfo.setLabelText(field.optString(Constants.LABEL));
        fieldInfo.setIsClass(field.optBoolean(Constants.CLASS_TYPE));
        fieldInfo.setVisible(field.optBoolean(Constants.VISIBLE));
        fieldInfo.setReadOnly(field.optBoolean(Constants.READ_ONLY));
        //field layout
        fieldInfo.setLayout(createLayoutProperties(field.optJSONObject(Constants.LAYOUT)));
        //rules
        JSONArray rules = field.optJSONArray(Constants.RULES);
        if(rules != null) {
            for (int i = 0; i < rules.length(); i++) {
                fieldInfo.addRule(createRule(rules.optJSONObject(i)));
            }
        }
        //options
        JSONArray options = field.optJSONArray(Constants.OPTIONS);
        if(options != null){
            for (int i = 0; i < options.length(); i++) {
                fieldInfo.addOption(createOption(options.optJSONObject(i)));
            }
        }

        return fieldInfo;
    }

    private LayoutProperties createLayoutProperties(JSONObject layoutJson) throws JSONException {
        LayoutProperties layoutProp = new LayoutProperties();
        if(layoutJson == null){
            layoutProp.setLayoutDefinition(LayoutDefinitions.ONECOLUMNLAYOUT);
            layoutProp.setLayoutOrientation(LayoutOrientation.AXISX);
            layoutProp.setLabelPosition(LabelPosition.BEFORE);
            return layoutProp; //with default values
        }

        try {
            String layDefName = layoutJson.optString(Constants.LAYOUT_DEF);
            LayoutDefinitions layDef = LayoutDefinitions.valueOf(layDefName);
            if(layDef != null){
                layoutProp.setLayoutDefinition(layDef);
            }

            String orientation = layoutJson.optString(Constants.LAYOUT_ORIENT);
            LayoutOrientation layOrient = LayoutOrientation.valueOf(orientation);
            if(layOrient != null){
                layoutProp.setLayoutOrientation(layOrient);
            }

            String position  = layoutJson.optString(Constants.LABEL_POS);

            LabelPosition labelPos = LabelPosition.valueOf(position);
            if (labelPos != null) {
                layoutProp.setLabelPosition(labelPos);
            }
        }catch(IllegalArgumentException e){
            System.err.println(e.getLocalizedMessage());
            //e.printStackTrace();
        }
        return layoutProp;
    }

    private ValidationRule createRule(JSONObject ruleJson) throws JSONException {
        if(ruleJson != null) {
            ValidationRule rule = new ValidationRule();
            rule.setValidationType(ruleJson.optString(Constants.VALIDATION_TYPE));
            rule.setValue(ruleJson.optString(Constants.VALUE));
            return rule;
        }else{
            return null;
        }
    }

    private FieldOption createOption(JSONObject optionJson) throws JSONException {
        if(optionJson != null) {
            FieldOption option = new FieldOption();
            option.setKey(optionJson.optString(Constants.KEY));
            option.setValue(optionJson.optString(Constants.VALUE));
            return option;
        }else{
            return null;
        }
    }
}
