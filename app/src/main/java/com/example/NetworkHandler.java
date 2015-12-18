package com.example;

/**
 * Created by shivam on 12/18/15.
 */

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * It sends data to server,  if successfully send then it removes that data from event queue list.
 * <p/>
 * Created by shivam on 4/9/15.
 */
public class NetworkHandler {

    public static String TAG = NetworkHandler.class.getName();
    public static final String SERVER_URL = "http://anylist.c.appier.net/l/a/";
    /**
     * It sends data to server,  if successfully send then it removes that data from event queue
     * list.
     *
     * @param data      data to send.
     * @return return success if request successfully executed  else return responsePhrase of
     * http status line or failed as response.
     */
    public static String execute( final String data) {

        URL url;

        try {
            url = new URL(SERVER_URL);
        } catch (MalformedURLException ex) {
            Log.e(TAG + "  " + NetworkHandler.class.getName(),
                    "malformed url exception message is " + ex.getMessage());
            return ex.getMessage();
        }

        try {


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(SERVER_URL);
            // using string entity
            httpPost.setEntity(new StringEntity(data));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            StatusLine statusLine = httpResponse.getStatusLine();
            Log.i(TAG + "  " + NetworkHandler.class.getName(),
                    "status code is  " + statusLine.getStatusCode());
            Log.i(TAG + "  " + NetworkHandler.class.getName(),
                    "response phrase  is  " + statusLine.getReasonPhrase());

            if (statusLine.getStatusCode() == 200) {
                return "success";
            }
            return statusLine.getReasonPhrase();

        } catch (UnsupportedEncodingException ex) {

            Log.e(TAG + "  " + NetworkHandler.class.getName(),
                    "unsupported encoding exception message is " + ex.getMessage());
            return ex.getMessage();

        } catch (IOException ex) {

            Log.e(TAG + "  " + NetworkHandler.class.getName(),
                    "io exception message is " + ex.getMessage());
            return ex.getMessage();

        }

    }

}