package com.example.nikita.SeekerApp;


import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtils {

    private final static String VK_BASE_URL =
            "https://api.vk.com/method/photos.search?";

    private final static String PARAM_LATTITUDE = "lat";

    private final static String PARAM_LONGTITUDE = "long";
    private final static String PARAM_RADIUS = "radius";
    private final static String PARAM_TOKEN = "access_token";
    private final static String PARAM_SORT = "sort";
    private final static String PARAM_COUNT = "count";


    public static URL buildUrl(String mLat, String mLong, String mRadius, String mToken) {
        Uri buildUri = Uri.parse(VK_BASE_URL).buildUpon().appendQueryParameter(PARAM_LATTITUDE, mLat)
                .appendQueryParameter(PARAM_LONGTITUDE, mLong).appendQueryParameter(PARAM_RADIUS, mRadius)
                .appendQueryParameter(PARAM_TOKEN, mToken).appendQueryParameter(PARAM_SORT, "0")
                .appendQueryParameter(PARAM_COUNT, "200").build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException mEx) {
            mEx.printStackTrace();
        }
        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static ArrayList<ArrayList<String>> getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                String response = scanner.next();
                JSONObject responseObject = new JSONObject(response);
                JSONArray responseArray = responseObject.getJSONArray("response");
                ArrayList<ArrayList<String>> outter = new ArrayList<>();
                ArrayList<String> inner;
                for( int i = 0; i <= 200; i++){
                    inner = new ArrayList<>();
                    JSONObject userInfoObject = responseArray.getJSONObject(i);
                    inner.add(userInfoObject.getString("owner_id"));
                    inner.add(userInfoObject.getString("src"));
                    outter.add(inner);
                }
                return outter;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return  null;
    }
}