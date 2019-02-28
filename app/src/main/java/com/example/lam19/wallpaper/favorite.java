package com.example.lam19.wallpaper;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class favorite extends AppCompatActivity {
    private ApdapterFavorite ApFavorite;
    ArrayList<arrUrlImage> arrListImage = new ArrayList<>();
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    Internet internet;
    NoInternetDialog noInternetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        swipeRefreshLayout = findViewById(R.id.swipeReloadFavorite);
        progressBar = findViewById(R.id.progressBarFavorite);
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorActionBar));
        noInternetDialog = new NoInternetDialog.Builder(this).setButtonColor(Color.parseColor("#fe3f80")).setDialogRadius((float) 50).setCancelable(true).build();
        //check internet
        internet = new Internet();
        if(internet.isNetworkAvailable(this)){
            progressBar.setVisibility(View.VISIBLE);
            getFavorite();
        }else{
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            FrameLayout frameLayout = findViewById(R.id.frameLayoutFavorite);
            Drawable drawable = getResources().getDrawable(R.mipmap.no_internet);
            frameLayout.setBackground(drawable);
        }
        RecyclerView rc = findViewById(R.id.recyclerViewFavorite);
        RecyclerView.LayoutManager layout = new GridLayoutManager(this,2);
        ApFavorite = new ApdapterFavorite(this,arrListImage);
        rc.setLayoutManager(layout);
        rc.setAdapter(ApFavorite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Favorite");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(internet.isNetworkAvailable(favorite.this)){
                    arrListImage.clear();
                    getFavorite();
                    ApFavorite.notifyDataSetChanged();
                    FrameLayout frameLayout = findViewById(R.id.frameLayoutFavorite);
                    Drawable drawable = getResources().getDrawable(R.color.black);
                    frameLayout.setBackground(drawable);
                }else{
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(noInternetDialog != null){
            System.out.println(noInternetDialog);
            try {
                noInternetDialog.onDestroy();
                noInternetDialog = null;
            }catch (Exception ex){
                System.out.println("Error" + ex.getMessage());
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                           ApFavorite.notifyDataSetChanged();
                           progressBar.setVisibility(View.GONE);
                           swipeRefreshLayout.setRefreshing(false);
                       }
                    }
                    @Override
                    public void onFailure(Call<resulltFeatured> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        FrameLayout frameLayout = findViewById(R.id.frameLayoutFavorite);
                        Drawable drawable = getResources().getDrawable(R.mipmap.background_favorite);
                        frameLayout.setBackground(drawable);
                    }
                });
    }
    // get info image by id
    public void Getdata(Integer id){
        System.out.println("id : ");
        System.out.println(id);
        Api.getRetrofit().create(Api.APIInfoImage.class)
                .getInfoImage("563492ad6f91700001000001bd5e05fa13044b96bb29ebd4a8a4c55f",id)
                .enqueue(new Callback<arrUrlImage>() {
                    @Override
                    public void onResponse(Call<arrUrlImage> call, Response<arrUrlImage> response) {
                        if(response.isSuccessful()){
//                            customAdapter.notifyDataSetChanged();
//                            swpiSwipeRefreshLayout.setRefreshing(false);

                        }else{
                            Log.e(" Error ",response.message());
                        }
                        ApFavorite.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<arrUrlImage> call, Throwable t) {

                    }
                });
    }
}
