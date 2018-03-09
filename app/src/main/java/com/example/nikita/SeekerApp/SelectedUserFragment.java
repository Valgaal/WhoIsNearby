package com.example.nikita.SeekerApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.UUID;
import java.util.regex.Pattern;

public class SelectedUserFragment extends Fragment {
    private static final String ARG_USER_ID = "user_id";
    private UserInfo mUser;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID userId = (UUID) getArguments().getSerializable(ARG_USER_ID);
        mUser = UserInfoList.get(getActivity()).getUserInfo(userId);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_user, container, false);
        ImageView mBigPhotoImageView = (ImageView) view.findViewById(R.id.bigUserImageView);
        Picasso.with(getActivity()).load(mUser.getBigPhoto_url()).into(mBigPhotoImageView);
        Button mShowUserButton = (Button) view.findViewById(R.id.buttonShowUser);
        mShowUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userStringUri = mUser.getVKId();
                Intent intent = new Intent(Intent.ACTION_VIEW, prepareUri(userStringUri) );
                startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to)));
            }
        });
        return view;
    }

    private Uri prepareUri(String userStringUri){
        if(userStringUri.charAt(0) == '-'){
            userStringUri = "https://vk.com/club" + userStringUri;
        }
        if(Pattern.matches("[a-zA-Z]+", userStringUri))
        {
            userStringUri = "https://vk.com/" + userStringUri;
        }
        if(Pattern.matches("[0-9]+", userStringUri))
        {
            userStringUri = "https://vk.com/id" + userStringUri;
        }
        return Uri.parse(userStringUri);
    }
    public static SelectedUserFragment newInstance(UUID userId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_ID, userId);

        SelectedUserFragment fragment = new SelectedUserFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
