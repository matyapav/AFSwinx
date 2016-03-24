using System;
using System.Collections.Generic;
using System.Globalization;
using AFWinPhone.enums;
using AFWinPhone.rest.connection;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using AFWinPhone.utils;

namespace AFWinPhoneTests
{
    [TestClass]
    public class UtilsTest
    {
        [TestMethod]
        public void TestParseDate()
        {
            //first format
            DateTime? expected = DateTime.Parse("2013-01-06T04:00:00.000+0000");
            DateTime? actual = Utils.ParseDate("2013-01-06T04:00:00.000+0000");
            Assert.AreEqual(actual, expected);

            //second format
            expected = DateTime.ParseExact("22.02.2016", "dd.MM.yyyy", CultureInfo.InvariantCulture);
            actual = Utils.ParseDate("22.02.2016");
            Assert.AreEqual(actual, expected);

            //bad formats should not be parsed and return null
            actual = Utils.ParseDate("2011-01-18ijii");
            Assert.IsNull(actual);
        }

        [TestMethod]
        public void TestGetConnectionEndPoint()
        {
            AFSwinxConnection connection = new AFSwinxConnection("example.com", 8080, "/AFServer/resource", "http");

            String actual = Utils.GetConnectionEndPoint(connection);
            String expected = "http://example.com:8080/AFServer/resource";
            Assert.AreEqual(actual, expected);

            //test without port
            connection.setPort(0);
            expected = "http://example.com/AFServer/resource";
            actual = Utils.GetConnectionEndPoint(connection);
            Assert.AreEqual(actual, expected);
        }

        [TestMethod]
        public void TestValueOfSuccess()
        {
            SupportedWidgets expected = SupportedWidgets.TEXTFIELD;
            SupportedWidgets actual = Utils.ValueOf<SupportedWidgets>(typeof(SupportedWidgets), "TEXTFIELD");
            Assert.AreEqual(actual, expected);
        }

        [TestMethod]
        public void TestValueOfFail()
        {
            //test non existing value
            SupportedWidgets actual = Utils.ValueOf<SupportedWidgets>(typeof(SupportedWidgets), "non existing value");
            Assert.IsNull(actual);
            //test null value
            actual = Utils.ValueOf<SupportedWidgets>(typeof (SupportedWidgets), null);
            Assert.IsNull(actual);
        }

    }
}
