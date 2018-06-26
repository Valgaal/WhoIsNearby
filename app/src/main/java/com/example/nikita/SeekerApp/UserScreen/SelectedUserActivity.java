package com.example.nikita.SeekerApp.UserScreen;

import android.support.v4.app.Fragment;

import com.example.nikita.SeekerApp.SingleFragmentActivity;

import java.util.UUID;

public class SelectedUserActivity extends SingleFragmentActivity {

    private static final String EXTRA_USER_ID ="user_id" ;

    @Override
    protected Fragment createFragment(){

        UUID userID = (UUID) getIntent().getSerializableExtra(EXTRA_USER_ID);
        return SelectedUserFragment.newInstance(userID);
    }
}
