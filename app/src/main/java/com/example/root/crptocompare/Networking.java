package com.example.root.crptocompare;

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

public class Networking {

    private Networking() {
    }

    public static final String LOG_TAG = Networking.class.getSimpleName();

    public static ArrayList<Currency> fetchCurrency(String str){

        String jsonResponse = null;

        URL url = createUrl(str);

        try {
            jsonResponse = makeUrlConnection(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOexeption",e);
        }

        return parseJson(jsonResponse);
    }

    public static String makeUrlConnection(URL url)throws IOException{
        InputStream inputStream = null;
        String jsonResponse = null;

        HttpURLConnection urlConnection = null;


        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                Log.i(LOG_TAG, "TEST: 200 Received");
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.i(LOG_TAG, "TEST Response: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.i(LOG_TAG, "TEST",e);
            e.printStackTrace();
        }finally {
            if(inputStream !=null){
                inputStream.close();
            }
            if(urlConnection != null){
            urlConnection.disconnect();
            }
        }

        return jsonResponse;
    }

    public static URL createUrl(String url){
        URL url1 = null;
        try {
             url1 = new URL(url);
        } catch (MalformedURLException e) {
            Log.i(LOG_TAG, "MalformedUrl",e);
        }

        return url1;
    }
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

    public static ArrayList<Currency> parseJson(String json){
        String[] arr = new String[]{"NGN","USD","AUD","GBP","JPY","CHF","AFN","DZD","AOA","ARS","BRL","XOF","CNY","SVC","ETB","HUF","NAD","NZD","NOK","PHP"};
        if(TextUtils.isEmpty(json)){
            return null;
        }
        ArrayList<Currency> arrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            for (String anArr : arr) {
                String curr = jsonObject.getString(anArr);
                arrayList.add(new Currency(anArr, curr));
            }
            Log.i(LOG_TAG, "TEST:PArse success ");
        } catch (JSONException e) {
            Log.i(LOG_TAG, "TEST: Error Parsing JSON");
        }

        return arrayList;
    }
}
