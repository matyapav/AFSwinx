package cz.cvut.fel.matyapav.afandroid.parsers;

import com.tomscz.afrest.commons.SupportedValidations;
import com.tomscz.afrest.commons.SupportedWidgets;
import com.tomscz.afrest.layout.Layout;
import com.tomscz.afrest.layout.TopLevelLayout;
import com.tomscz.afrest.layout.definitions.LabelPosition;
import com.tomscz.afrest.layout.definitions.LayouDefinitions;
import com.tomscz.afrest.layout.definitions.LayoutOrientation;
import com.tomscz.afrest.rest.dto.AFClassInfo;
import com.tomscz.afrest.rest.dto.AFFieldInfo;
import com.tomscz.afrest.rest.dto.AFOptions;
import com.tomscz.afrest.rest.dto.AFValidationRule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.cvut.fel.matyapav.afandroid.utils.Constants;

/**
 * Parser for component definition in JSON format
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class JSONDefinitionParser implements JSONParser<AFClassInfo> {

    @Override
    public AFClassInfo parse(String classInfoJson, boolean parsingInnerClass){

        AFClassInfo definition = null;

        try {
            JSONObject classInfo = new JSONObject(classInfoJson);
            if(!parsingInnerClass){
                classInfo = classInfo.optJSONObject(Constants.CLASS_INFO);
            }
            //Parse class name and create data pack with this class name
            //System.err.println("PARSING CLASS "+ classInfo.getString(Constants.CLASS_NAME));
            definition = new AFClassInfo();
            System.err.println("PARSING CLASS " + classInfo.getString(Constants.CLASS_NAME));
            definition.setName(classInfo.getString(Constants.CLASS_NAME));

            //Parse layout
            JSONObject layout = classInfo.optJSONObject(Constants.LAYOUT);
            definition.setLayout(createTopLayoutProperties(layout));

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

    /**
     * Parses information about one field in component.
     *
     * @param field field information wrapped in JSONObject
     * @return field info wrapped in {@link AFFieldInfo} object
     * @throws JSONException thrown if there war error during parsing field info
     */
    private AFFieldInfo parseFieldInfo(JSONObject field) throws JSONException {
        System.out.println("PARSING FIELD " + field.getString(Constants.ID));
        AFFieldInfo fieldInfo = new AFFieldInfo();
        try {
            fieldInfo.setWidgetType(SupportedWidgets.valueOf(field.optString(Constants.WIDGET_TYPE)));
        }catch (IllegalArgumentException e){
            System.err.println(e.getLocalizedMessage());
        }
        fieldInfo.setId(field.getString(Constants.ID));

        fieldInfo.setLabel(field.get(Constants.LABEL).equals(null) ? null : field.get(Constants.LABEL).toString());
        fieldInfo.setClassType(field.optBoolean(Constants.CLASS_TYPE));
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

    /**
     * Parses and creates layout properties of whole component
     *
     * @param layoutJson JSON object with layout properties
     * @return Layout of whole component
     * @throws JSONException thrown if any error happened during parsing layout properties.
     */
    private TopLevelLayout createTopLayoutProperties(JSONObject layoutJson) throws JSONException {
        TopLevelLayout layoutProp = new TopLevelLayout();
        if(layoutJson == null){
            layoutProp.setLayoutDefinition(LayouDefinitions.ONECOLUMNLAYOUT);
            layoutProp.setLayoutOrientation(LayoutOrientation.AXISX);
            return layoutProp; //with default values
        }

        try {
            String layDefName = layoutJson.optString(Constants.LAYOUT_DEF);
            LayouDefinitions layDef = LayouDefinitions.valueOf(layDefName);
            if(layDef != null){
                layoutProp.setLayoutDefinition(layDef);
            }

            String orientation = layoutJson.optString(Constants.LAYOUT_ORIENT);
            LayoutOrientation layOrient = LayoutOrientation.valueOf(orientation);
            if(layOrient != null){
                layoutProp.setLayoutOrientation(layOrient);
            }
        }catch(IllegalArgumentException e){
            System.err.println(e.getLocalizedMessage());
            //e.printStackTrace();
        }
        return layoutProp;
    }

    /**
     * Parses and creates layout properties of component field.
     *
     * @param layoutJson JSON object with layout properties
     * @return layout of the field
     * @throws JSONException thrown if any error happened during parsing layout properties.
     */
    private Layout createLayoutProperties(JSONObject layoutJson) throws JSONException{
        Layout layoutProp = new Layout();
        if(layoutJson == null){
            layoutProp.setLayoutDefinition(LayouDefinitions.ONECOLUMNLAYOUT);
            layoutProp.setLayoutOrientation(LayoutOrientation.AXISX);
            layoutProp.setLabelPosstion(LabelPosition.BEFORE);
            return layoutProp; //with default values
        }

        try {
            String layDefName = layoutJson.optString(Constants.LAYOUT_DEF);
            LayouDefinitions layDef = LayouDefinitions.valueOf(layDefName);
            if(layDef != null){
                layoutProp.setLayoutDefinition(layDef);
            }

            String orientation = layoutJson.optString(Constants.LAYOUT_ORIENT);
            LayoutOrientation layOrient = LayoutOrientation.valueOf(orientation);
            if(layOrient != null){
                layoutProp.setLayoutOrientation(layOrient);
            }

            String labelPosition = layoutJson.optString(Constants.LABEL_POS);
            LabelPosition position = LabelPosition.valueOf(labelPosition);
            if(position != null){
                layoutProp.setLabelPosstion(position);
            }
        }catch(IllegalArgumentException e){
            System.err.println(e.getLocalizedMessage());
            //e.printStackTrace();
        }
        return layoutProp;
    }

    /**
     * Parses and creates validation rule
     *
     * @param ruleJson JSON object which contains info about validation rule
     * @return created validation rule
     * @throws JSONException thrown if any error happened during parsing validation rule.
     */
    private AFValidationRule createRule(JSONObject ruleJson) throws JSONException {
        if(ruleJson != null) {
            String validationType = ruleJson.optString(Constants.VALIDATION_TYPE);
            String value = ruleJson.optString(Constants.VALUE);
            return new AFValidationRule(SupportedValidations.valueOf(validationType), value);
        }else{
            return null;
        }
    }

    /**
     * Parses and creates option, from which user can choose.
     *
     * @param optionJson JSON object which contains info about option
     * @return created option
     * @throws JSONException thrown if any error happened during parsing option.
     */
    private AFOptions createOption(JSONObject optionJson) throws JSONException {
        if(optionJson != null) {
            String key = optionJson.optString(Constants.KEY);
            String value = optionJson.optString(Constants.VALUE);
            return new AFOptions(key, value);
        }else{
            return null;
        }
    }
}
