package com.example.nikita.SeekerApp;


import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    private final static String PARAM_VERSION = "version";


    public static URL buildUrl(String mLat, String mLong, String mRadius, String mToken) {
        Uri buildUri = Uri.parse(VK_BASE_URL).buildUpon().appendQueryParameter(PARAM_LATTITUDE, mLat)
                .appendQueryParameter(PARAM_LONGTITUDE, mLong).appendQueryParameter(PARAM_RADIUS, mRadius)
                .appendQueryParameter(PARAM_TOKEN, mToken).appendQueryParameter(PARAM_SORT, "0")
                .appendQueryParameter(PARAM_COUNT, "200").appendQueryParameter(PARAM_VERSION, "5.73").build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException mEx) {
            mEx.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next(); //response in String format
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return  null;
    }
}