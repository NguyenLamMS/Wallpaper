package com.example.lam19.wallpaper;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.os.Handler;
import android.os.IBinder;

import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceWallpaper extends Service {
    Bitmap bitmap;
    Handler handler;
    Random r;
    Integer i;
    SharedPreferences sp;
    private Handler h;
    private Runnable runnable;
    int time;
    String linkImage;
    String select;
    Internet internet;
    ArrayList<arrUrlImage> arrListImage = new ArrayList<>();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        internet = new Internet();
        sp=getSharedPreferences("WallpaperAuto", MODE_PRIVATE);
        r = new Random();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().contains("start")) {
          try{
              h = new Handler();
              runnable = new Runnable() {
                  @Override
                  public void run() {
                      time = sp.getInt("time",24);
                      System.out.println("Time : " + time);
                      select = sp.getString("select","Favorite");
                      //Random
                      i =  r.nextInt(1000);

                      //check time upfate wallpaper
                      if(time != -1){
                          h.postDelayed(this, time*3600000);

                      }else{
                          h.postDelayed(this, 10000);
                      }
                      if(internet.isNetworkAvailable(ServiceWallpaper.this)) {
                          // check get image from favorite or random
                          if (select.equals("Favorite")) {
                              getFavorite();
                          } else {
                              if (select.equals("Random")) {
                                  resulUrlImageRandom(1, i);
                              }
                          }
                      }else{
                          System.out.println("no internet");
                      }
                      startForeground(1, updateNotification());

                  }
              };

              h.post(runnable);
          }catch (Exception e){
              Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show();
          }
        } else {
            h.removeCallbacks(runnable);
            stopForeground(true);
            stopSelf();
        }

        return Service.START_STICKY;
    }
    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public void autoUpdateWallapper(final String url){
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = Glide.
                            with(ServiceWallpaper.this).
                            asBitmap().
                            load(url).
                            into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).
                            get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // apply wallpaper
                                WallpaperManager.getInstance(getApplicationContext()).setBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Notification updateNotification() {
        String info = "Running change wallpaper over time";

        Context context = getApplicationContext();

        PendingIntent action = PendingIntent.getActivity(context,
                0, new Intent(context, Setting.class),
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String CHANNEL_ID = "wallpaper";

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Wallpaper",
                    NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Change wallpaper over time");
            manager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        }
        else
        {
            builder = new NotificationCompat.Builder(context);
        }

        return builder.setContentIntent(action)
                .setContentTitle("Wallpaper")
                .setTicker(info)
                .setContentText(info)
                .setSmallIcon(R.drawable.icon_status_bar)
                .setContentIntent(action)
                .setOngoing(true).build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    public void resulUrlImageRandom(int query, int page){
        Api.getRetrofit().create(Api.API.class)
                .searchImage("563492ad6f91700001000001bd5e05fa13044b96bb29ebd4a8a4c55f",query,page)
                .enqueue(new Callback<resulltFeatured>() {
                    @Override
                    public void onResponse(Call<resulltFeatured> call, Response<resulltFeatured> response) {
                        if(response.isSuccessful()){
                            linkImage = response.body().getPhotto().get(0).src.portrait;
                            if(linkImage != null){
                                autoUpdateWallapper(linkImage);
                            }
                        }else{
                            Log.e(" Error ",response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<resulltFeatured> call, Throwable t) {
                        Log.e(" Error ",t.getMessage());
                        t.printStackTrace();
                    }
                });
    }

    public void getFavorite(){
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        Integer id =sp1.getInt("id", -1);
        MyApi.getRetrofit().create(MyApi.ApiGetFavorite.class)
                .GetFavorite(id)
                .enqueue(new Callback<resulltFeatured>() {
                    @Override
                    public void onResponse(Call<resulltFeatured> call, Response<resulltFeatured> response) {
                        if(response.isSuccessful()){
                            arrListImage.addAll(response.body().getPhotto());
                            i =  r.nextInt(arrListImage.size());
                            if(arrListImage.get(i).src.portrait != null){
                                autoUpdateWallapper(arrListImage.get(i).src.portrait);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<resulltFeatured> call, Throwable t) {

                    }
                });
    }
}
