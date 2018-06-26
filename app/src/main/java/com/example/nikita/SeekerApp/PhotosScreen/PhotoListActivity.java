package com.example.nikita.SeekerApp.PhotosScreen;

import android.support.v4.app.Fragment;

import com.example.nikita.SeekerApp.SingleFragmentActivity;

public class PhotoListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return  new PhotoListFragment();
    }
}