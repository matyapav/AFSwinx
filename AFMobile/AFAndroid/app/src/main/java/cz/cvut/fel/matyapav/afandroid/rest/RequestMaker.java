package cz.cvut.fel.matyapav.afandroid.rest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;

import com.tomscz.afswinx.rest.connection.ConnectionSecurity;
import com.tomscz.afswinx.rest.connection.HeaderType;
import com.tomscz.afswinx.rest.connection.HttpMethod;
import com.tomscz.afswinx.rest.connection.SecurityMethod;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Created by Pavel on 14.02.2016.
 */
public class RequestMaker extends AsyncTask<String,Integer,Object> {

    HeaderType headerType;
    HttpMethod httpMethod;
    ConnectionSecurity security;
    String address;
    Object data;

    public RequestMaker(HttpMethod method, HeaderType headerType,
                        ConnectionSecurity security, Object data, String url) {
        this.headerType = headerType;
        this.httpMethod = method;
        this.security = security;
        this.address = url;
        this.data = data;
    }

    @Override
    protected Object doInBackground(String... params) {
        InputStream inputStream;
        String response = null;
        HttpURLConnection urlConnection = null;
        try {
            System.err.println("URL " + address);
            System.err.println("HTTP METHOD " + httpMethod.toString().toUpperCase());
            System.err.println("HEADER TYPE " + headerType);

            URL url = new URL(address);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod(httpMethod.toString().toUpperCase());
            urlConnection.setRequestProperty("CONTENT-TYPE", headerType.toString()); //TODO there can be also another parameters .. not only content-type
            urlConnection.setDoInput(true);

            if (security != null) {
                if (security.getMethod().equals(SecurityMethod.BASIC)) {
                    String encoded = Base64.encodeToString((security.getUserName() + ":" + security.getPassword()).getBytes(), Base64.NO_WRAP);
                    urlConnection.setRequestProperty("Authorization", "Basic " + encoded);
                    System.err.println("SECURITY " + "Basic " + encoded);
                }
            }

            if (data != null && (httpMethod.equals(HttpMethod.POST) || httpMethod.equals(HttpMethod.PUT))) {
                System.err.println("DATA " + data.toString());
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                os.write(data.toString().getBytes("UTF-8"));
                os.close();

            }

            int responseCode = urlConnection.getResponseCode();
            String responseMsg = urlConnection.getResponseMessage();
            System.err.println("RESPONSE CODE " + responseCode);
            if (responseCode < 200 || responseCode >= 300) {
                throw new ConnectException(responseCode+" "+responseMsg);
            } else {
                inputStream = urlConnection.getInputStream();
            }
            if (inputStream != null) {
                response = Utils.convertInputStreamToString(inputStream);
                System.err.println("RESPONSE IS " + response);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
