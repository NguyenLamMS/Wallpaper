package com.example.lam19.wallpaper;

import android.content.Intent;
import android.content.SharedPreferences;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;

import android.os.Bundle;


import com.github.paolorotolo.appintro.AppIntro;

import com.github.paolorotolo.appintro.AppIntroFragment;




public class Wellcome extends AppIntro{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        Integer id =sp1.getInt("id", -1);
        String email = sp1.getString("email", null);

        if(email != null && id != -1){
            Intent myIntent=new Intent(Wellcome.this, Home.class);
            startActivity(myIntent);
            finish();
        }
//        setContentView(R.layout.activity_wellcome);
//        setFadeAnimation();
//        setZoomAnimation();
//        setFlowAnimation();
//        setSlideOverAnimation();
        setDepthAnimation();
        addSlide(AppIntroFragment.newInstance("Wellcome","\n" +
                "Welcome to the wallpaper",R.mipmap.logo, ContextCompat.getColor(getApplicationContext(),R.color.bg_screen1)));
        addSlide(AppIntroFragment.newInstance("Wallpaper","We offer you new wallpapers every day",R.drawable.ic_upload_cloud_flat, ContextCompat.getColor(getApplicationContext(),R.color.bg_screen2)));
        addSlide(AppIntroFragment.newInstance("Set Wallpaper","You can set wallpaper directly by application",R.drawable.ic_settings_flat, ContextCompat.getColor(getApplicationContext(),R.color.bg_screen3)));
        addSlide(AppIntroFragment.newInstance("Download Wallpaper","You can download the wallpaper to your phone to share the wallpaper for your friends",R.drawable.ic_download_cloud_flat, ContextCompat.getColor(getApplicationContext(),R.color.bg_screen4)));

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent myIntent=new Intent(Wellcome.this, Login.class);
        startActivity(myIntent);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent myIntent=new Intent(Wellcome.this, Login.class);
        startActivity(myIntent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        int currItem = getPager().getCurrentItem();
        switch (currItem){
            case 0:
                getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.bg_screen1));
                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.bg_screen1));
                break;
            case 1:
                getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.bg_screen2));
                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.bg_screen2));
                break;
            case 2:
                getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.bg_screen3));
                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.bg_screen3));
                break;
            case 3:
                getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.bg_screen4));
                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.bg_screen4));
        }
    }

}
