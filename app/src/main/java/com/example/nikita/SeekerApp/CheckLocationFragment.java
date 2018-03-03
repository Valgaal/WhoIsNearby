package com.example.nikita.SeekerApp;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CheckLocationFragment extends Fragment{
    private TextView mLatitude;
    private TextView mLongitude;
    private static final int TAG_CODE_PERMISSION_LOCATION = 0;
    private static final String TOKEN = "Token";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checklocation, container, false);

        mLatitude = (TextView) v.findViewById(R.id.latitudeTextView);
        mLongitude = (TextView) v.findViewById(R.id.longtitudeTextView);
        Button mShow = (Button) v.findViewById(R.id.showButton);
        Button mLocationButton = (Button) v.findViewById(R.id.buttonGPS);
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocation();
            }
        });
        mShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PhotoListActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

    public void checkLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    TAG_CODE_PERMISSION_LOCATION);
        }
        Intent intent = new Intent(getActivity(), gpsTracker.class);
        getActivity().startService(intent);

        IntentFilter statusIntentFilter = new IntentFilter(
                gpsTracker.BROADCAST_ACTION);
        CheckLocationFragment.GpsDataReceiver mGpsDataReceiver = new CheckLocationFragment.GpsDataReceiver();
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mGpsDataReceiver, statusIntentFilter);

    }
    private class GpsDataReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        private GpsDataReceiver() {
        }
        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        @Override
        public void onReceive(Context context, Intent intent) {
            String cords[] = intent.getStringArrayExtra(gpsTracker.EXTENDED_DATA);
            mLatitude.setText(cords[0]);
            mLongitude.setText(cords[1]);
            SharedPreferences settings = getActivity().getSharedPreferences("Token & Location", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(getString(R.string.latitude), cords[0]);
            editor.putString(getString(R.string.longitude), cords[1]);
            editor.commit();
        }
    }
}
