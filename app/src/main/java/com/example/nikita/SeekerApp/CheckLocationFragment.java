package com.example.nikita.SeekerApp;

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
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class CheckLocationFragment extends Fragment{
    private TextView mLatitude;
    private TextView mLongitude;
    private static final String TOKEN = "Token";
    String lat;
    String lon;
    WebView myWebView;

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
                String webUrl = myWebView.getUrl();
                if(webUrl.contains("/@")){
                    String[] tokens = webUrl.split("/@");
                    String[] tokens1 = tokens[1].split(",");
                    lat = tokens1[0];
                    lon = tokens1[1];
                }
                mycords(lat,lon);
            }
        });


        myWebView = (WebView) v.findViewById(R.id.mapWebView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.setWebChromeClient(new MyWebChromeClient());
        String link = "http://maps.google.com/";
        myWebView.loadUrl(link);

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

    public void mycords(String lat, String lon){
        mLatitude.setText(lat);
        mLongitude.setText(lon);
        SharedPreferences settings = getActivity().getSharedPreferences("Token & Location", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getString(R.string.latitude), lat);
        editor.putString(getString(R.string.longitude), lon);
        editor.commit();
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
            mycords(cords[0], cords[1]);
        }
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {

            view.loadUrl(url);
            return true;
        }
    }

    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            // Geolocation permissions coming from this app's Manifest will only be valid for devices with API_VERSION < 23.
            // On API 23 and above, we must check for permission, and possibly ask for it.
            callback.invoke(origin, true, false);
        }
    }
}
