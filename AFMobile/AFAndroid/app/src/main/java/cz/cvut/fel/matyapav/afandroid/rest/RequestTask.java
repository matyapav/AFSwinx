package cz.cvut.fel.matyapav.afandroid.rest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Created by Pavel on 14.02.2016.
 */
public class RequestTask extends AsyncTask<String,Integer,Object> {

    ProgressDialog progressDialog;
    Activity activity;

    HeaderType headerType;
    HttpMethod httpMethod;
    ConnectionSecurity security;
    String address;
    Object data;

    public RequestTask(final Activity activity, HttpMethod method, HeaderType headerType,
                       ConnectionSecurity security, Object data, String url) {
        this.activity = activity;
        this.headerType = headerType;
        this.httpMethod = method;
        this.security = security;
        this.address = url;
        this.data = data;

        //TODO vymyslet co s timhle
        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(Localization.translate("please.wait", activity));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });
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
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
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

        @Override
    protected void onPostExecute(final Object response){
        super.onPostExecute(response);
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });

    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else{
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
