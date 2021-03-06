package com.example.nikita.SeekerApp.models;

import java.util.UUID;

public class UserInfo {
    private String photo_url;
    private UUID mId;
    private String mVKId;
    private String bigPhoto_url;

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

    public void setBigPhoto_url(String photo_url) {
        this.bigPhoto_url = photo_url;
    }

    public String getBigPhoto_url() {
        return bigPhoto_url;
    }
}
