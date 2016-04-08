package cz.cvut.fel.matyapav.afandroid;

import com.tomscz.afswinx.rest.connection.AFSwinxConnection;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.cvut.fel.matyapav.afandroid.utils.Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Pavel on 24.03.2016.
 */
public class UtilsTest {

    @Test
    public void testParseDate() throws Exception {
        //first format
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date expected = format.parse("2013-01-06T04:00:00.000+0000");
        Date actual = Utils.parseDate("2013-01-06T04:00:00.000+0000");
        assertEquals(actual, expected);

        //second format
        format = new SimpleDateFormat("dd.MM.yyyy");
        expected = format.parse("22.02.2016");
        actual = Utils.parseDate("22.02.2016");
        assertEquals(actual, expected);

        //another formats should return null
        actual = Utils.parseDate("2011-01-18 00:00:00.0");
        assertNull(actual);
        System.out.println("Parsing date test PASSED");
    }

    @Test
    public void testGetConnectionEndPoint() throws Exception{
        AFSwinxConnection connection = new AFSwinxConnection("example.com", 8080, "/AFServer/resource", "http");

        String actual = Utils.getConnectionEndPoint(connection);
        String expected = "http://example.com:8080/AFServer/resource";
        assertEquals(actual, expected);

        //test without port
        connection.setPort(0);
        expected = "http://example.com/AFServer/resource";
        actual = Utils.getConnectionEndPoint(connection);
        assertEquals(actual, expected);
    }
}
