package com.example.lam19.wallpaper;

import android.content.Context;

import android.graphics.Color;
import android.net.ConnectivityManager;

import android.support.v7.app.AppCompatActivity;

import am.appwise.components.ni.NoInternetDialog;


public class Internet extends AppCompatActivity {
    NoInternetDialog noInternetDialog;
    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
