package com.example.nikita.SeekerApp.PhotosScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikita.SeekerApp.GetPhotosNearbyDataTask;
import com.example.nikita.SeekerApp.NetworkUtils;
import com.example.nikita.SeekerApp.R;
import com.example.nikita.SeekerApp.ResponseCallback;
import com.example.nikita.SeekerApp.UserScreen.SelectedUserActivity;
import com.example.nikita.SeekerApp.models.UserInfo;
import com.example.nikita.SeekerApp.models.UserInfoList;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class PhotoListFragment extends Fragment implements ResponseCallback {

    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photolist, container, false);
        mPhotoRecyclerView = (RecyclerView) view.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager( new GridLayoutManager(getActivity(),2));
        new GetPhotosNearbyDataTask(this).execute(makeUrl());
        return view;

    }

    private URL makeUrl(){
        SharedPreferences settings = getActivity().getSharedPreferences("Token & Location", 0);
        String mToken = settings.getString(getActivity().getString(R.string.token),"");
        String mLatitude = settings.getString(getActivity().getString(R.string.latitude),"");
        String mLongitude = settings.getString(getActivity().getString(R.string.longitude),"");
        URL vkSearch = NetworkUtils.buildUrl(mLatitude, mLongitude, "500", mToken);
        return vkSearch;
    }

    private void updateUI(List mInfoList){
        if ( mAdapter == null) {
            mAdapter = new PhotoAdapter(mInfoList);
            mPhotoRecyclerView.setAdapter(mAdapter);
        } else{
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void response(List response) {
        UserInfoList userInfoList = UserInfoList.get(getActivity());
        userInfoList.setUserInfos(response);
        updateUI(response);
    }

    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String EXTRA_USER_ID ="user_id";
        private TextView mIdTextView;
        private ImageView mPhotoImageView;
        private UserInfo mUserInfo;

        public PhotoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_photo, parent, false));

            mIdTextView = (TextView) itemView.findViewById(R.id.userIdTextView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
            mIdTextView.setOnClickListener(this);
            mPhotoImageView.setOnClickListener(this);
        }

        public void bind(UserInfo userInfo) {
            mUserInfo = userInfo;
            mIdTextView.setText(mUserInfo.getVKId());
            Picasso.with(getActivity()).load(mUserInfo.getPhoto_url()).into(mPhotoImageView);/// mPhotoImageView picasso
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), SelectedUserActivity.class);
            intent.putExtra(EXTRA_USER_ID, mUserInfo.getId());
            startActivity(intent);
        }
    }

    public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<UserInfo> mUserInfos;

        public PhotoAdapter(List<UserInfo> userInfos) {
            mUserInfos = userInfos;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PhotoHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            UserInfo userInfo = mUserInfos.get(position);
            holder.bind(userInfo);
        }

        @Override
        public int getItemCount() {
            return mUserInfos.size();
        }
    }
}
