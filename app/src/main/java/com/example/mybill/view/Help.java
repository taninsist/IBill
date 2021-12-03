package com.example.mybill.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;

import com.example.mybill.R;

public class Help extends AppCompatActivity {

    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        webView = (WebView) findViewById(R.id.webView);

//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("file:android_asset/help.html");
        webView.loadUrl("file:android_asset/help.html");


    }
}