package com.example.nikita.SeekerApp;

import java.util.UUID;

public class UserInfo {
    private String photo_url;
    private UUID mId;
    private String mVKId;

    public UserInfo(){
        mId = UUID.randomUUID();
    }

    public UUID getId(){
        return mId;
    }

    public String getVKId(){
        return mVKId;
    }

    public void setVKid(String user_id) {
        this.mVKId = user_id;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getPhoto_url() {
        return photo_url;
    }
}
