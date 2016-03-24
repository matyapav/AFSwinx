using System;
using AFWinPhone;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Test
{
    [TestClass]
    public class ParserTest
    {
        [TestMethod]
        public void TestMethod1()
        {
            JSONParser parser = new JSONDefinitionParser();
            String wellFormedPersonJson = getWellFormedJson();
            ClassDefinition actual = parser.parse(wellFormedPersonJson, false);

            //test parsing class basic info
            assertEquals(actual.getClassName(), "person");
            assertEquals(actual.getLayout().getLayoutDefinition(), LayoutDefinitions.ONECOLUMNLAYOUT);
            assertEquals(actual.getLayout().getLayoutOrientation(), LayoutOrientation.AXISX);
            assertEquals(actual.getFieldInfos().size(), 12);
            //test parsing field
            FieldInfo firstField = actual.getFieldInfos().get(0);
            assertEquals(firstField.getWidgetType(), SupportedWidgets.TEXTFIELD);
            assertEquals(firstField.getId(), "login");
            assertEquals(firstField.getLabelText(), "Login");
            assertFalse(firstField.isReadOnly());
            assertFalse(firstField.isInnerClass());
            assertTrue(firstField.isVisible());
            assertEquals(firstField.getLayout().getLabelPosition(), LabelPosition.BEFORE);
            assertEquals(firstField.getRules().size(), 2);
            //test parsing rule
            ValidationRule firstRule = firstField.getRules().get(0);
            assertEquals(firstRule.getValidationType(), SupportedValidations.REQUIRED.getValidationType());
            assertEquals(firstRule.getValue(), "true");
            //test parsing options
            assertNull(firstField.getOptions());
            //test parsing innerclasses
            assertEquals(actual.getInnerClasses().size(), 1);
            assertEquals(actual.getInnerClasses().get(0).getClassName(), "myAddress");
            assertEquals(actual.getInnerClasses().get(0).getFieldInfos().size(), 5);
            System.out.println("Parsing date test PASSED");

        }
    }
}
