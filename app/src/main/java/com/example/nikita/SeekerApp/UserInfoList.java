package com.example.nikita.SeekerApp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserInfoList {
    private static UserInfoList sUserInfoList;
    private List<UserInfo> mUserInfos;

    public static UserInfoList get(Context context){
        if (sUserInfoList == null) {
            sUserInfoList = new UserInfoList(context);
        }
        return sUserInfoList;
    }
    private UserInfoList(Context context){
        mUserInfos = new ArrayList<>();

        SharedPreferences settings = context.getSharedPreferences("Token & Location", 0);
        String mToken = settings.getString(context.getString(R.string.token),"");
        String mLatitude = settings.getString(context.getString(R.string.latitude),"");
        String mLongitude = settings.getString(context.getString(R.string.longitude),"");
        URL vkSearch = NetworkUtils.buildUrl(mLatitude, mLongitude, "500", mToken);

        new UserInfoList.GetPhotosNearbyData().execute(vkSearch);
    }

    public List<UserInfo> getUserInfos(){
        return  mUserInfos;
    }

    public UserInfo getUserInfo(UUID id){
        for ( UserInfo userInfo: mUserInfos){
            if (userInfo.getId().equals(id)){
                return userInfo;
            }
        }
        return null;
    }


    private class GetPhotosNearbyData extends AsyncTask<URL, Void, List > {

        @Override
        protected List doInBackground(URL... urls) {
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
                    mUserInfos.add(userInfoFromWeb);
                }
                return mUserInfos;
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List userData){
        }
    }
}
