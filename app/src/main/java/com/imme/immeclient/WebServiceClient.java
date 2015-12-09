package com.imme.immeclient;

import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by santo on 23-Oct-15.
 */
public class WebServiceClient {
    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int DATARETRIEVAL_TIMEOUT = 5000;

    public static JSONObject requestWebService(String serviceUrl) throws IOException {
        JSONObject result = new JSONObject();

        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            urlConnection.setConnectTimeout(WebServiceClient.CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(WebServiceClient.DATARETRIEVAL_TIMEOUT);

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                result = WebServiceClient.createErrorResponse(statusCode, "Authrorized Error");
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                result = WebServiceClient.createErrorResponse(statusCode, "Error raised.");
            }

            // create JSON object from content
            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());
            result = new JSONObject(getResponseText(in));
        } catch (MalformedURLException e) {
            //TODO: add Url Exception log mechanism here
        } catch (SocketTimeoutException e) {
            //TODO: add Socket Timeout Exception log mechanism here and if posible put some retry logic
        } catch (IOException e) {
            //TODO: add IO Exception log mechanism here, Input Output issue raised
        } catch (JSONException e) {
            //TODO: add Json Exception log mechanism here
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

    private static JSONObject createErrorResponse(int code, String message) {
        JSONObject result = new JSONObject();

        try {
            result.put("code", code);
            result.put("message", message);
        } catch (JSONException jex) {
            //TODO: add Json Exception log mechanism here
        }

        return result;
    }
}

