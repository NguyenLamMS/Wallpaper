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
    ArrayList<String> listImageURLsMedium = new ArrayList<>();
    ArrayList<String> listImageURLsOriginal = new ArrayList<>();
    ArrayList<Integer> listIdImage = new ArrayList<>();
    private  ArrayList<String> listNameImge = new ArrayList<>();
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
        ApFavorite = new ApdapterFavorite(this,listImageURLsMedium,listIdImage,listNameImge,listImageURLsOriginal);
        rc.setLayoutManager(layout);
        rc.setAdapter(ApFavorite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Favorite");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(internet.isNetworkAvailable(favorite.this)){
                    listIdImage.clear();
                    listImageURLsMedium.clear();
                    listImageURLsOriginal.clear();
                    listNameImge.clear();
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
            System.out.println("ahihi");
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
                .enqueue(new Callback<ArrListFavorite>() {
                    @Override
                    public void onResponse(Call<ArrListFavorite> call, Response<ArrListFavorite> response) {
                       if(response.isSuccessful()){
                           listIdImage.addAll(response.body().getListFavorite());
                           listImageURLsMedium.addAll(response.body().getListImageMedium());
                           listNameImge.addAll(response.body().getListName());
                           listImageURLsOriginal.addAll(response.body().getListImageOriginal());
                           ApFavorite.notifyDataSetChanged();
                           progressBar.setVisibility(View.GONE);
                           swipeRefreshLayout.setRefreshing(false);
                       }
                    }

                    @Override
                    public void onFailure(Call<ArrListFavorite> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        FrameLayout frameLayout = findViewById(R.id.frameLayoutFavorite);
                        Drawable drawable = getResources().getDrawable(R.mipmap.background_favorite);
                        frameLayout.setBackground(drawable);
                    }
                });
    }
    public void Getdata(Integer id){
        System.out.println("id : ");
        System.out.println(id);
        Api.getRetrofit().create(Api.APIInfoImage.class)
                .getInfoImage("563492ad6f91700001000001bd5e05fa13044b96bb29ebd4a8a4c55f",id)
                .enqueue(new Callback<arrUrlImage>() {
                    @Override
                    public void onResponse(Call<arrUrlImage> call, Response<arrUrlImage> response) {
                        if(response.isSuccessful()){
//                            if(isClean == true){
//                                listImageURLsOriginal.clear();
//                                listImageURLsMedium.clear();
//                                listIdImage.clear();

//                            }
//                            for (arrUrlImage  a: apiUrlImage) {
                                listImageURLsMedium.add(response.body().src.medium);
                                listImageURLsOriginal.add(response.body().src.original);
                                listIdImage.add(response.body().id);
                                listNameImge.add(response.body().namePhoto);
                                System.out.println(listImageURLsMedium);
//                            }
//                            customAdapter.notifyDataSetChanged();
//                            swpiSwipeRefreshLayout.setRefreshing(false);

                        }else{
                            System.out.println(listImageURLsOriginal.size());
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
