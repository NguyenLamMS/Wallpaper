package com.example.lam19.wallpaper;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Setting extends AppCompatActivity {
    LinearLayout linearLayout;
    LinearLayout linearLayout2;
    LinearLayout linearLayout3;
    CheckBox checkBox;
    TextView txtTime;
    TextView txtFrom;
    SharedPreferences sp;
    int Time;
    String select;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Setting");
        //create button back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences("WallpaperAuto", MODE_PRIVATE);
        txtFrom = findViewById(R.id.txtAfterCell3);
        txtTime = findViewById(R.id.txtAfterCell2);
        linearLayout3 = findViewById(R.id.cell1);
        checkBox = findViewById(R.id.cbTheme);
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorActionBar));
        //get  int to string
        Time = Integer.parseInt(txtTime.getText().toString().replaceAll("[\\D]",""));
        select = txtFrom.getText().toString();
        txtTime.setText(sp.getInt("time",24) + " hours");
        if(sp.getInt("time",1) == -1){
            txtTime.setText("10 seconds");
        }
        txtFrom.setText(sp.getString("select","Favorite"));
        //click cell 1
        linearLayout3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    linearLayout3.setBackgroundColor(getResources().getColor(R.color.primaryLightColorBlueGray));
                    if(checkBox.isChecked()){
                        checkBox.setChecked(false);
                        linearLayout2.setEnabled(false);
                        linearLayout.setEnabled(false);
                    }else{
                        checkBox.setChecked(true);
                        linearLayout2.setEnabled(true);
                        linearLayout.setEnabled(true);
                    }
                }
                linearLayout3.setBackgroundColor(getResources().getColor(R.color.black));
                return false;
            }
        });


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(checkBox.isChecked()){
//                    checkBox.setChecked(false);
//                    linearLayout2.setEnabled(false);
//                    linearLayout.setEnabled(false);
//                }else{
//                    checkBox.setChecked(true);
//                    linearLayout2.setEnabled(true);
//                    linearLayout.setEnabled(true);
//                }
            }
        });

        clickCellOne();
        clickCellTwo();
        checkBoxIsCheck(sp);

    }
    public void clickCellOne(){
        linearLayout = findViewById(R.id.cell2);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.primaryLightColorBlueGray));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.black));
                }
                return false;
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] fonts = {"24 hours", "10 hours", "1 hours", "10 seconds"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                builder.setTitle("Time update wallpaper");
                builder.setItems(fonts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("24 hours".equals(fonts[which])){
                            txtTime.setText("24 hours");
                        }
                        else if ("10 hours".equals(fonts[which])){
                            txtTime.setText("10 hours");
                        }
                        else if ("1 hours".equals(fonts[which])){
                            txtTime.setText("1 hours");
                        }
                        else if ("10 seconds".equals(fonts[which])){
                            txtTime.setText("10 seconds");
                        }
                    }
                });
                builder.show();
            }
        });
    }
    public void clickCellTwo(){
        linearLayout2 = findViewById(R.id.cell3);
        linearLayout2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    linearLayout2.setBackgroundColor(getResources().getColor(R.color.primaryLightColorBlueGray));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    linearLayout2.setBackgroundColor(getResources().getColor(R.color.black));
                }
                return false;
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] fonts = {"Favorite", "Random"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                builder.setTitle("Select photos from");
                builder.setItems(fonts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("Favorite".equals(fonts[which])){
                            txtFrom.setText("Favorite");
                        }
                        else if ("Random".equals(fonts[which])){
                            txtFrom.setText("Random");
                        }
                    }
                });
                builder.show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if(item.getItemId() == R.id.btnSave){
            if(checkBox.isChecked()) {
                if(txtTime.getText().toString().contains("hours")){
                    Time = Integer.parseInt(txtTime.getText().toString().replaceAll("[\\D]", ""));
                }else{
                    if(txtTime.getText().toString().contains("seconds")){
                        Time = -1;
                    }
                }
                select = txtFrom.getText().toString();
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putInt("status",1);
                Ed.putInt("time",Time);
                Ed.putString("select",select);
                Ed.commit();
                if(isMyServiceRunning(ServiceWallpaper.class)){
                    stop();
                }else{
                    start();
                }
            }else{
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putInt("status",0);
                Ed.commit();
                stop();
            }
            Toast.makeText(this, "Saved settings", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkBoxIsCheck(SharedPreferences sp){
        int status = sp.getInt("status",0);
        if(status == 1){
            checkBox.setChecked(true);
            linearLayout2.setEnabled(true);
            linearLayout.setEnabled(true);
        }else{
            checkBox.setChecked(false);
            linearLayout2.setEnabled(false);
            linearLayout.setEnabled(false);
        }
    }

    public void start() {
        if (isMyServiceRunning(ServiceWallpaper.class)) return;
        Intent startIntent = new Intent(this, ServiceWallpaper.class);
        startIntent.setAction("start");
        startService(startIntent);
    }
    public void stop() {
        if (!isMyServiceRunning(ServiceWallpaper.class)) return;
        Intent stopIntent = new Intent(this, ServiceWallpaper.class);
        stopIntent.setAction("stop");
        startService(stopIntent);
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sp.getInt("status",0) == 1){
            start();
        }else{
            stop();
        }
    }
}
