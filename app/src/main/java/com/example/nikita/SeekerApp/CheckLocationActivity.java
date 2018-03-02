package com.example.nikita.SeekerApp;

import android.support.v4.app.Fragment;

public class CheckLocationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return  new CheckLocationFragment();
    }
}
