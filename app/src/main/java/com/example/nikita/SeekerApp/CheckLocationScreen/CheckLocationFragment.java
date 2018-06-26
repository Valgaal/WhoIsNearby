package com.example.nikita.SeekerApp.CheckLocationScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nikita.SeekerApp.PhotosScreen.PhotoListActivity;
import com.example.nikita.SeekerApp.R;
import com.example.nikita.SeekerApp.WebViewActivity;
import com.example.nikita.SeekerApp.gpsTracker;

public class CheckLocationFragment extends Fragment{
    private TextView mLatitude;
    private TextView mLongitude;
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
        Button mMapCordsButton = (Button) v.findViewById(R.id.buttonMapGps);
        mMapCordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                startActivityForResult(intent,1);
            }
        });

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
        Intent intent = new Intent(getActivity(), gpsTracker.class);
        getActivity().startService(intent);

        IntentFilter statusIntentFilter = new IntentFilter(
                gpsTracker.BROADCAST_ACTION);
        CheckLocationFragment.GpsDataReceiver mGpsDataReceiver = new CheckLocationFragment.GpsDataReceiver();
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mGpsDataReceiver, statusIntentFilter);

    }

    public void saveMyCords(String lat, String lon){
        mLatitude.setText(lat);
        mLongitude.setText(lon);
        SharedPreferences settings = getActivity().getSharedPreferences("Token & Location", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getString(R.string.latitude), lat);
        editor.putString(getString(R.string.longitude), lon);
        editor.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            Bundle coordinatesBundle = data.getExtras();
            String[] coordinates = coordinatesBundle.getStringArray("Coordinates");
            saveMyCords(coordinates[0],coordinates[1]);
        }
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
            saveMyCords(cords[0], cords[1]);
        }
    }
}
