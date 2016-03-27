using System;
using System.Collections.Generic;
using System.Diagnostics;
using AFWinPhone.components.parts;
using AFWinPhone.enums;
using AFWinPhone.parsers;
using AFWinPhone.utils;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace AFWinPhoneTests
{
    [TestClass]
    public class ParserTest
    {
        [TestMethod]
        public void TestParseJsonFileWellFormed()
        {
            JSONParser parser = new JSONDefinitionParser();
            String wellFormedPersonJson = GetWellFormedJson();
            AFClassInfo actual = parser.parse(wellFormedPersonJson, false);

            //test parsing class basic info
            
            Assert.AreEqual(actual.getClassName(), "person");
            Assert.AreEqual(actual.getLayout().getLayoutDefinition(), LayoutDefinitions.ONECOLUMNLAYOUT);
            Assert.AreEqual(actual.getLayout().getLayoutOrientation(), LayoutOrientation.AXISX);
            Assert.AreEqual(actual.getFieldInfos().Count, 12);
            //test parsing field
            AFFieldInfo firstField = actual.getFieldInfos()[0];
            Assert.AreEqual(firstField.getWidgetType(), SupportedWidgets.TEXTFIELD);
            Assert.AreEqual(firstField.getId(), "login");
            Assert.AreEqual(firstField.getLabelText(), "Login");
            Assert.IsFalse(firstField.isReadOnly());
            Assert.IsFalse(firstField.isInnerClass());
            Assert.IsTrue(firstField.isVisible());
            Assert.AreEqual(firstField.getLayout().getLabelPosition(), LabelPosition.BEFORE);
            Assert.AreEqual(firstField.getRules().Count, 2);
            //test parsing rule
            AFValidationRule firstRule = firstField.getRules()[0];
            Assert.AreEqual(firstRule.getValidationType(), SupportedValidations.REQUIRED.getValidationType());
            Assert.AreEqual(firstRule.getValue(), "true");
            //test parsing options
            Assert.IsNull(firstField.getOptions());
            //test parsing innerclasses
            Assert.AreEqual(actual.getInnerClasses().Count, 1);
            Assert.AreEqual(actual.getInnerClasses()[0].getClassName(), "myAddress");
            Assert.AreEqual(actual.getInnerClasses()[0].getFieldInfos().Count, 5);
            Debug.WriteLine("Parsing date test PASSED");

        }
        
        [TestMethod]
        public void TestParseJsonFileCorrupted()
        {
            JSONParser parser = new JSONDefinitionParser();
            String corruptedPersonJson = "{\"classInfo\":{\"name\":\"person\",\"****CORRUPTED FILE****";
            AFClassInfo actual = parser.parse(corruptedPersonJson, false);
            Assert.IsNull(actual);
        }

        [TestMethod]
        public void TestStringElParser()
        {
            // Create parameters which will replaced in input string
            Dictionary<String, String> parameters = new Dictionary<String, String>();
            String id = "23";
            String location = "Prague";
            parameters.Add("id", id);
            parameters.Add("location", location);

            Assert.AreEqual(Utils.evaluateElExpression("#{error}", parameters), "null");
            Assert.AreEqual(Utils.evaluateElExpression("/AFServer/rest/Person/#{id}", parameters), "/AFServer/rest/Person/" + id);
            Assert.AreEqual(Utils.evaluateElExpression("/AFServer/rest#{location}/Person/#{id}", parameters), "/AFServer/rest" + location + "/Person/" + id);
            Assert.AreEqual(Utils.evaluateElExpression("#{location}/AFServer/rest#{location}/Person/#{id}", parameters), location + "/AFServer/rest" + location + "/Person/" + id);
            Assert.AreEqual(Utils.evaluateElExpression("{}#{location}{noreplace}is#{id}", parameters), "{}" + location + "{noreplace}is" + id);
            Assert.AreEqual(Utils.evaluateElExpression("{}#{location}{noreplace}{noreplace}is#{id}", parameters), "{}" + location + "{noreplace}{noreplace}is" + id);
            Assert.AreEqual(Utils.evaluateElExpression("{}{{{}}}", parameters), "{}{{{}}}");
        }

        private String GetWellFormedJson()
        {
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
}
