package com.example.nikita.SeekerApp;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;


public class gpsTracker extends IntentService implements LocationListener{

    public static final String BROADCAST_ACTION =
            "com.example.android.threadsample.BROADCAST";
    public static final String EXTENDED_DATA =
            "gpsData";

    Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;

    double latitude;
    double longitude;

    public LocationManager locationManager;
    Location location;

    public gpsTracker() {
        super("gpsTracker");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        getLocation();
    }



    public Location getLocation() {

        try {
            locationManager = (LocationManager) gpsTracker.this.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (isNetworkEnabled) {
                    try {
                        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, gpsTracker.this, null);
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {

                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                    } catch (SecurityException sE){
                        sE.printStackTrace();
                    }
                } else if (isGPSEnabled){
                    try {
                        if (location == null) {
                            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, gpsTracker.this, null);
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                        }
                    } catch (SecurityException sE){
                        sE.printStackTrace();
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }


        String[] cords = new String[2];
        cords[0] = Double.toString(latitude);
        cords[1] = Double.toString(longitude);
        Intent localIntent =
                new Intent(gpsTracker.BROADCAST_ACTION)
                        .putExtra(gpsTracker.EXTENDED_DATA, cords );
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

        return location;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
