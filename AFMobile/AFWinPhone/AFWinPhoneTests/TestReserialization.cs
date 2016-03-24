using System;
using AFWinPhone.rest.connection;
using AFWinPhone.rest.holder;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace AFWinPhoneTests
{
    [TestClass]
    public class TestReserialization
    {
        [TestMethod]
        public void TestJsonReserializationSuccess()
        {
            AFDataHolder dataToTest = new AFDataHolder();
            dataToTest.addPropertyAndValue("name", "Pavel");
            dataToTest.addPropertyAndValue("lastName", "Matyáš");
            AFDataHolder innerClass = new AFDataHolder();
            innerClass.setClassName("adress");
            innerClass.addPropertyAndValue("Street", "Zikova");
            dataToTest.addInnerClass(innerClass);

            BaseRestBuilder restBuilder = new JSONBuilder();
            Object json = restBuilder.reselialize(dataToTest);
            Assert.IsNotNull(json);
        }
    }
}
