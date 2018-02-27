package com.example.nikita.SeekerApp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    TextView mLocation1;
    TextView mLocation2;
    private static final int TAG_CODE_PERMISSION_LOCATION = 0;
    private static final String TOKEN = "Token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKSdk.initialize(getApplicationContext());
        VKSdk.login(SecondActivity.this, VKScope.OFFLINE);
        setContentView(R.layout.activity_second);
        mLocation1 = (TextView) findViewById(R.id.locationTextView);
        mLocation2 = (TextView) findViewById(R.id.location2TextView);
        Button mLocationButton = (Button) findViewById(R.id.buttonGPS);
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocation();
            }
        });
        // добавить RecyclerView с адаптером
        // заполнить адаптер
    }

    public void checkLocation() {
        if (ContextCompat.checkSelfPermission(SecondActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(SecondActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(SecondActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    TAG_CODE_PERMISSION_LOCATION);
        }
        Intent intent = new Intent(SecondActivity.this, gpsTracker.class);
        startService(intent);

        IntentFilter statusIntentFilter = new IntentFilter(
                gpsTracker.BROADCAST_ACTION);
        SecondActivity.GpsDataReceiver mGpsDataReceiver = new SecondActivity.GpsDataReceiver();
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(SecondActivity.this).registerReceiver(
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
            mLocation1.setText(cords[0]);
            mLocation2.setText(cords[1]);
            SharedPreferences settings = getSharedPreferences("Token", MODE_PRIVATE);
            String mToken = settings.getString(TOKEN,"");
            URL vkSearch = NetworkUtils.buildUrl(cords[0], cords[1], "500", mToken);
            new GetPhotosNearbyData().execute(vkSearch);


        }
    }
    private class GetPhotosNearbyData extends AsyncTask<URL, Void, ArrayList<ArrayList<String>> >{

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(URL... urls) {
            ArrayList<ArrayList<String>> userData;
            URL mUrl = urls[0];
            try {
                userData = NetworkUtils.getResponseFromHttpUrl(mUrl);
                return userData;
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> userData){
        }
    }
}
