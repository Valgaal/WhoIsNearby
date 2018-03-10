package com.example.nikita.SeekerApp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetPhotosNearbyDataTask extends AsyncTask<URL, Void, List > {

    private ResponseCallback callback;

    public GetPhotosNearbyDataTask (ResponseCallback callback) {
        this.callback = callback;
    }

    @Override
    protected List doInBackground(URL... urls) {
        ArrayList mr = new ArrayList();
        String response;
        URL mUrl = urls[0];
        try {
            response = NetworkUtils.getResponseFromHttpUrl(mUrl);
            JSONObject responseObject = new JSONObject(response);
            JSONArray responseArray = responseObject.getJSONArray("response");
            for( int i = 1; i <= 100; i++){// start from 1 because zero position is number in the response
                UserInfo userInfoFromWeb = new UserInfo();
                JSONObject userInfoObject = responseArray.getJSONObject(i);
                userInfoFromWeb.setVKid(userInfoObject.getString("owner_id"));
                userInfoFromWeb.setPhoto_url(userInfoObject.getString("src_big"));
                if(userInfoObject.has("src_xxxbig")) {
                    userInfoFromWeb.setBigPhoto_url(userInfoObject.getString("src_xxxbig"));
                } else if(userInfoObject.has("src_xxbig")) {
                    userInfoFromWeb.setBigPhoto_url(userInfoObject.getString("src_xxbig"));
                }

                mr.add(userInfoFromWeb);
            }
            return mr;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List userData){
        callback.response(userData);
    }
}
