package com.example.nikita.SeekerApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WebViewActivity extends AppCompatActivity {
    WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        myWebView = (WebView) findViewById(R.id.mapWebView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        myWebView.setWebViewClient(new WebViewActivity.MyWebViewClient());
        myWebView.setWebChromeClient(new WebViewActivity.MyWebChromeClient());
        String link = "http://maps.google.com/";
        myWebView.loadUrl(link);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String webUrl = myWebView.getUrl();
                if(webUrl.contains("/@")){
                    String[] splitURL = webUrl.split("/@");
                    String[] coordinates = splitURL[1].split(",");
                    Intent i = new Intent();
                    Bundle coordsFromMap = new Bundle();
                    coordsFromMap.putStringArray("Coordinates", coordinates);
                    i.putExtras(coordsFromMap);
                    setResult(1,i);
                    finish();
                }
            }
        });
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
