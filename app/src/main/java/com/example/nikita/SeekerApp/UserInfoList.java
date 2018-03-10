package com.example.nikita.SeekerApp;


import android.content.Context;
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
       // mUserInfos = new ArrayList<>();
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.mUserInfos = userInfos;
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
}
