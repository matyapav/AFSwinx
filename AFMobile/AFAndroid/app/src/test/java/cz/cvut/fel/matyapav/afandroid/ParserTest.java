package cz.cvut.fel.matyapav.afandroid;

import com.tomscz.afrest.commons.SupportedValidations;
import com.tomscz.afrest.commons.SupportedWidgets;
import com.tomscz.afrest.layout.definitions.LabelPosition;
import com.tomscz.afrest.layout.definitions.LayouDefinitions;
import com.tomscz.afrest.layout.definitions.LayoutOrientation;
import com.tomscz.afrest.rest.dto.AFClassInfo;
import com.tomscz.afrest.rest.dto.AFFieldInfo;
import com.tomscz.afrest.rest.dto.AFValidationRule;

import org.junit.Test;

import cz.cvut.fel.matyapav.afandroid.parsers.JSONDefinitionParser;
import cz.cvut.fel.matyapav.afandroid.parsers.JSONParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pavel on 24.03.2016.
 */
public class ParserTest {

    @Test
    public void testParseFieldInfo() throws Exception {

        JSONParser parser = new JSONDefinitionParser();
        String wellFormedPersonJson = getWellFormedJson();
        AFClassInfo actual = parser.parse(wellFormedPersonJson, false);

        //test parsing class basic info
        assertEquals(actual.getName(), "person");
        assertEquals(actual.getLayout().getLayoutDefinition(), LayouDefinitions.ONECOLUMNLAYOUT);
        assertEquals(actual.getLayout().getLayoutOrientation(), LayoutOrientation.AXISX);
        assertEquals(actual.getFieldInfo().size(), 12);
        //test parsing field
        AFFieldInfo firstField = actual.getFieldInfo().get(0);
        assertEquals(firstField.getWidgetType(), SupportedWidgets.TEXTFIELD);
        assertEquals(firstField.getId(), "login");
        assertEquals(firstField.getLabel(), "Login");
        assertFalse(firstField.getReadOnly());
        assertFalse(firstField.getClassType());
        assertTrue(firstField.getVisible());
        assertEquals(firstField.getLayout().getLabelPosstion(), LabelPosition.BEFORE);
        assertEquals(firstField.getRules().size(), 2);
        //test parsing rule
        AFValidationRule firstRule = firstField.getRules().get(0);
        assertEquals(firstRule.getValidationType(), SupportedValidations.REQUIRED);
        assertEquals(firstRule.getValue(), "true");
        //test parsing options
        assertNull(firstField.getOptions());
        //test parsing innerclasses
        assertEquals(actual.getInnerClasses().size(), 1);
        assertEquals(actual.getInnerClasses().get(0).getName(), "myAddress");
        assertEquals(actual.getInnerClasses().get(0).getFieldInfo().size(), 5);
        System.out.println("Parsing date test PASSED");
    }

    @Test
    public void testParseFieldInfo_Corrupted() throws Exception{
        JSONParser parser = new JSONDefinitionParser();
        String corruptedPersonJson = "{\"classInfo\":{\"name\":\"person\",\"****CORRUPTED FILE****";
        AFClassInfo actual = parser.parse(corruptedPersonJson, false);
        assertNull(actual);
    }

    private String getWellFormedJson(){
        return "{\"classInfo\":{\"name\":\"person\",\"layout\":{\"layoutDefinition\":" +
                "\"ONECOLUMNLAYOUT\",\"layoutOrientation\":\"AXISX\"},\"fieldInfo\":[{\"widgetType" +
                "\":\"TEXTFIELD\",\"id\":\"login\",\"label\":\"Login\",\"classType\":false,\"readO" +
                "nly\":false,\"visible\":true,\"layout\":{\"layoutDefinition\":\"ONECOLUMNLAYOUT\"" +
                ",\"layoutOrientation\":\"AXISY\",\"labelPosstion\":\"BEFORE\"},\"rules\":[{\"vali" +
                "dationType\":\"REQUIRED\",\"value\":\"true\"},{\"validationType\":\"MAXLENGTH\"," +
                "\"value\":\"255\"}],\"options\":null},{\"widgetType\":\"PASSWORD\",\"id\":\"passw" +
                "ord\",\"label\":\"Password\",\"classType\":false,\"readOnly\":false,\"visible\":t" +
                "rue,\"layout\":{\"layoutDefinition\":\"ONECOLUMNLAYOUT\",\"layoutOrientation\":\"" +
                "AXISY\",\"labelPosstion\":\"BEFORE\"},\"rules\":[{\"validationType\":\"REQUIRED\"" +
                ",\"value\":\"true\"},{\"validationType\":\"MAXLENGTH\",\"value\":\"255\"}],\"opti" +
                "ons\":null},{\"widgetType\":\"TEXTFIELD\",\"id\":\"firstName\",\"label\":\"person" +
                ".firstName\",\"classType\":false,\"readOnly\":false,\"visible\":true,\"layout\":{" +
                "\"layoutDefinition\":\"ONECOLUMNLAYOUT\",\"layoutOrientation\":\"AXISY\",\"labelP" +
                "osstion\":\"BEFORE\"},\"rules\":[{\"validationType\":\"REQUIRED\",\"value\":\"tru" +
                "e\"},{\"validationType\":\"MAXLENGTH\",\"value\":\"255\"}],\"options\":null},{\"w" +
                "idgetType\":\"TEXTFIELD\",\"id\":\"lastName\",\"label\":\"person.lastName\",\"cla" +
                "ssType\":false,\"readOnly\":false,\"visible\":true,\"layout\":{\"layoutDefinition" +
                "\":\"ONECOLUMNLAYOUT\",\"layoutOrientation\":\"AXISY\",\"labelPosstion\":\"BEFORE" +
                "\"},\"rules\":[{\"validationType\":\"MAXLENGTH\",\"value\":\"255\"}],\"options\":" +
                "null},{\"widgetType\":\"NUMBERFIELD\",\"id\":\"age\",\"label\":\"person.age\",\"c" +
                "lassType\":false,\"readOnly\":false,\"visible\":true,\"layout\":{\"layoutDefiniti" +
                "on\":\"ONECOLUMNLAYOUT\",\"layoutOrientation\":\"AXISY\",\"labelPosstion\":\"BEFO" +
                "RE\"},\"rules\":[{\"validationType\":\"REQUIRED\",\"value\":\"true\"},{\"validati" +
                "onType\":\"MIN\",\"value\":\"15\"},{\"validationType\":\"MAX\",\"value\":\"60\"}]" +
                ",\"options\":null},{\"widgetType\":null,\"id\":\"myAddress\",\"label\":null,\"cla" +
                "ssType\":true,\"readOnly\":false,\"visible\":true,\"layout\":{\"layoutDefinition\"" +
                ":null,\"layoutOrientation\":null,\"labelPosstion\":null},\"rules\":null,\"options" +
                "\":null},{\"widgetType\":\"OPTION\",\"id\":\"active\",\"label\":\"Active\",\"clas" +
                "sType\":false,\"readOnly\":false,\"visible\":true,\"layout\":{\"layoutDefinition\"" +
                ":\"ONECOLUMNLAYOUT\",\"layoutOrientation\":\"AXISY\",\"labelPosstion\":\"BEFORE\"" +
                "},\"rules\":[{\"validationType\":\"REQUIRED\",\"value\":\"true\"}],\"options\":nu" +
                "ll},{\"widgetType\":\"CHECKBOX\",\"id\":\"confidentialAgreement\",\"label\":\"Con" +
                "fidential Agreement\",\"classType\":false,\"readOnly\":false,\"visible\":true,\"l" +
                "ayout\":{\"layoutDefinition\":\"ONECOLUMNLAYOUT\",\"layoutOrientation\":\"AXISY\"," +
                "\"labelPosstion\":\"BEFORE\"},\"rules\":null,\"options\":null},{\"widgetType\":\"T" +
                "EXTFIELD\",\"id\":\"email\",\"label\":\"Email\",\"classType\":false,\"readOnly\":" +
                "false,\"visible\":true,\"layout\":{\"layoutDefinition\":\"ONECOLUMNLAYOUT\",\"lay" +
                "outOrientation\":\"AXISY\",\"labelPosstion\":\"BEFORE\"},\"rules\":[{\"validation" +
                "Type\":\"MAXLENGTH\",\"value\":\"255\"}],\"options\":null},{\"widgetType\":\"OPTION" +
                "\",\"id\":\"gender\",\"label\":\"Gender\",\"classType\":false,\"readOnly\":false," +
                "\"visible\":true,\"layout\":{\"layoutDefinition\":\"ONECOLUMNLAYOUT\",\"layoutOri" +
                "entation\":\"AXISY\",\"labelPosstion\":\"BEFORE\"},\"rules\":[{\"validationType\"" +
                ":\"REQUIRED\",\"value\":\"true\"}],\"options\":[{\"key\":\"MALE\",\"value\":\"MAL" +
                "E\"},{\"key\":\"FEMALE\",\"value\":\"FEMALE\"}]},{\"widgetType\":\"CALENDAR\",\"i" +
                "d\":\"hireDate\",\"label\":\"Hire Date\",\"classType\":false,\"readOnly\":false,\"" +
                "visible\":true,\"layout\":{\"layoutDefinition\":\"ONECOLUMNLAYOUT\",\"layoutOrien" +
                "tation\":\"AXISY\",\"labelPosstion\":\"BEFORE\"},\"rules\":null,\"options\":null}" +
                ",{\"widgetType\":\"NUMBERFIELD\",\"id\":\"id\",\"label\":\"Id\",\"classType\":fal" +
                "se,\"readOnly\":true,\"visible\":false,\"layout\":{\"layoutDefinition\":null,\"la" +
                "youtOrientation\":null,\"labelPosstion\":null},\"rules\":null,\"options\":null}]," +
                "\"innerClasses\":[{\"name\":\"myAddress\",\"layout\":{\"layoutDefinition\":\"ONEC" +
                "OLUMNLAYOUT\",\"layoutOrientation\":\"AXISX\"},\"fieldInfo\":[{\"widgetType\":\"T" +
                "EXTFIELD\",\"id\":\"street\",\"label\":\"Street\",\"classType\":false,\"readOnly\"" +
                ":false,\"visible\":true,\"layout\":{\"layoutDefinition\":\"ONECOLUMNLAYOUT\",\"la" +
                "youtOrientation\":\"AXISY\",\"labelPosstion\":\"BEFORE\"},\"rules\":[{\"validatio" +
                "nType\":\"REQUIRED\",\"value\":\"true\"},{\"validationType\":\"MAXLENGTH\",\"valu" +
                "e\":\"255\"}],\"options\":null},{\"widgetType\":\"TEXTFIELD\",\"id\":\"city\",\"l" +
                "abel\":\"City\",\"classType\":false,\"readOnly\":false,\"visible\":true,\"layout\"" +
                ":{\"layoutDefinition\":\"ONECOLUMNLAYOUT\",\"layoutOrientation\":\"AXISY\",\"labe" +
                "lPosstion\":\"BEFORE\"},\"rules\":[{\"validationType\":\"REQUIRED\",\"value\":\"t" +
                "rue\"},{\"validationType\":\"MAXLENGTH\",\"value\":\"255\"}],\"options\":null},{\"" +
                "widgetType\":\"NUMBERFIELD\",\"id\":\"postCode\",\"label\":\"Post Code\",\"classT" +
                "ype\":false,\"readOnly\":false,\"visible\":true,\"layout\":{\"layoutDefinition\":" +
                "\"ONECOLUMNLAYOUT\",\"layoutOrientation\":\"AXISY\",\"labelPosstion\":\"BEFORE\"}" +
                ",\"rules\":[{\"validationType\":\"REQUIRED\",\"value\":\"true\"}],\"options\":nul" +
                "l},{\"widgetType\":\"DROPDOWNMENU\",\"id\":\"country\",\"label\":\"Country\",\"cl" +
                "assType\":false,\"readOnly\":false,\"visible\":true,\"layout\":{\"layoutDefinitio" +
                "n\":\"ONECOLUMNLAYOUT\",\"layoutOrientation\":\"AXISY\",\"labelPosstion\":\"BEFOR" +
                "E\"},\"rules\":[{\"validationType\":\"REQUIRED\",\"value\":\"true\"}],\"options\"" +
                ":[{\"key\":\"Denmark\",\"value\":\"Denmark\"},{\"key\":\"Czech republic\",\"value" +
                "\":\"Czech republic\"},{\"key\":\"United Kingdom\",\"value\":\"United Kingdom\"}," +
                "{\"key\":\"Poland \",\"value\":\"Poland \"},{\"key\":\"Slovakia\",\"value\":\"Slov" +
                "akia\"},{\"key\":\"Switzerland\",\"value\":\"Switzerland\"}]},{\"widgetType\":\"NU" +
                "MBERFIELD\",\"id\":\"id\",\"label\":\"Id\",\"classType\":false,\"readOnly\":true,\"" +
                "visible\":false,\"layout\":{\"layoutDefinition\":null,\"layoutOrientation\":null,\"" +
                "labelPosstion\":null},\"rules\":null,\"options\":null}],\"innerClasses\":null}]}}";

    }

}
