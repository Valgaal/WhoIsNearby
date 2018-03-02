package com.example.nikita.SeekerApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PhotoListFragment extends Fragment {

    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photolist, container, false);
        mPhotoRecyclerView = (RecyclerView) view.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager( new GridLayoutManager(getActivity(),2));

        updateUI();
        return view;
    }

    private void updateUI(){
        UserInfoList userInfoList = UserInfoList.get(getActivity());
        List<UserInfo>  userInfos = userInfoList.getUserInfos();
        mAdapter = new PhotoAdapter(userInfos);
        mPhotoRecyclerView.setAdapter(mAdapter);
    }

    private class PhotoHolder extends RecyclerView.ViewHolder{

        private TextView mIdTextView;
        private ImageView mPhotoImageView;
        private UserInfo mUserInfo;

        public PhotoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_photo, parent, false));

            mIdTextView = (TextView) itemView.findViewById(R.id.userIdTextView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
        }

        public void bind(UserInfo userInfo) {
            mUserInfo = userInfo;
            mIdTextView.setText(mUserInfo.getVKId());
           /// mPhotoImageView picasso
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
