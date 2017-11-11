package com.example.root.crptocompare;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by root on 11/4/17.
 */

/**
 * Helper methods related to requesting and receiving currency rates data from cryptocompare api.
 */

public class Networking {

    public static final String LOG_TAG = Networking.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link Networking} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name Networking (and an object instance of Networking is not needed).
     */
    private Networking() {
    }

    /**
     * @param str The link in a string format
     * Return a list of {@link Currency} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Currency> fetchCurrency(String str) {

        String jsonResponse = null;
        // create a urlobject from the url string
        URL url = createUrl(str);

        try {
            jsonResponse = makeUrlConnection(url);
        } catch (IOException e) {
            //Log.e(LOG_TAG, "IOexeption", e);
        }

        return parseJson(jsonResponse, str);
    }

    /**
     * @param url the url object
     *  Makes an httpConnection to the url and returns a JsonString
     * */

    public static String makeUrlConnection(URL url) throws IOException {
        InputStream inputStream = null;
        String jsonResponse = null;

        HttpURLConnection urlConnection = null;


        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                //Log.i(LOG_TAG, "TEST: 200 Received");
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                //Log.i(LOG_TAG, "TEST Response: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            //Log.i(LOG_TAG, "TEST", e);
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return jsonResponse;
    }

    /**
     * @param url string from which the url object is created
     * */
    public static URL createUrl(String url) {
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            //Log.i(LOG_TAG, "MalformedUrl", e);
        }

        return url1;
    }
    /**
     * Helper Method to convert the bytes/chunks of data received into a valid string
     * */

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * @param url The url string is used here to get the currencies requested from the cryptocompare api
     *            and is split into an array and used to get the keys in the jsonObject
     * */
    public static ArrayList<Currency> parseJson(String json, String url) {
        String a = url.substring(url.indexOf("NGN"), url.length());
        String[] arr = a.split(",");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        ArrayList<Currency> arrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject btc = jsonObject.getJSONObject("BTC");
            JSONObject eth = jsonObject.getJSONObject("ETH");
            for (String anArr : arr) {
                String bitCurrency = btc.optString(anArr);
                String ethCurrency = eth.optString(anArr);
                if(bitCurrency != null && !TextUtils.isEmpty(bitCurrency)){
                    arrayList.add(new Currency(anArr, bitCurrency, ethCurrency));
                }
            }
            //Log.i(LOG_TAG, "TEST:PArse success ");
        } catch (JSONException e) {
            //Log.i(LOG_TAG, "TEST: Error Parsing JSON");
        }

        return arrayList;
    }
}
