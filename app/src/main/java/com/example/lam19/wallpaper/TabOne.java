package com.example.lam19.wallpaper;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.NonNull;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.GridView;

import android.widget.ProgressBar;



import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabOne extends android.support.v4.app.Fragment {
    private ApdapterFavorite ApdapterHome;
    ArrayList<Integer> listIdImage = new ArrayList<Integer>();
    RecyclerView rc;
    SwipeRefreshLayout swpiSwipeRefreshLayout;
    Boolean userScroll = false;
    int page = 1;
    int query = 16;
    ProgressBar progressBar;
    ProgressBar progressBarCenter;
    Internet internet;
    View layout;
    FrameLayout frameLayout;
    private  ArrayList<arrUrlImage> apiUrlImage = new ArrayList<>();
    public ArrayList<arrUrlImage> getApiUrlImage() {
        return apiUrlImage;
    }

    public void setApiUrlImage(ArrayList<arrUrlImage> apiUrlImage) {
        this.apiUrlImage = apiUrlImage;
    }

    public TabOne() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_tab_one,container,false);
        frameLayout = layout.findViewById(R.id.frameLayoutTabOne);
        //check internet
        internet = new Internet();
        progressBarCenter = layout.findViewById(R.id.progressBarHomeCenter);
        progressBarCenter.setVisibility(View.VISIBLE);
        progressBar = layout.findViewById(R.id.progressBarHome);
        //check internet
        if(internet.isNetworkAvailable(getActivity())){
            resulUrlImageTabOne(16,1,true);
        }else{
            progressBarCenter.setVisibility(View.GONE);
            Drawable drawable = getResources().getDrawable(R.mipmap.no_internet);
            frameLayout.setBackground(drawable);
            swpiSwipeRefreshLayout.setRefreshing(false);
        }

        rc = layout.findViewById(R.id.recyclerViewHome);
        RecyclerView.LayoutManager layoutRC = new GridLayoutManager(getActivity(),2);
        ApdapterHome = new ApdapterFavorite(getActivity(),apiUrlImage);
        rc.setLayoutManager(layoutRC);
        rc.setAdapter(ApdapterHome);
        initScrollListener();
        swpiSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeReload);
        //func reload data using swipe down
        swpiSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(internet.isNetworkAvailable(getActivity())){
                            resulUrlImageTabOne(16,1,true);
                            Drawable drawable = getResources().getDrawable(R.color.black);
                            frameLayout.setBackground(drawable);
                        }else{
                            progressBarCenter.setVisibility(View.GONE);
                            swpiSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
        );

        return layout;
    }
    private void initScrollListener(){
        rc.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if(!userScroll){
                    if(gridLayoutManager != null && gridLayoutManager.findLastCompletelyVisibleItemPosition() == apiUrlImage.size() -1){
                        userScroll = true;
                        loadMode();
                    }
                }
            }
        });
    }
    private void loadMode(){
        progressBar.setVisibility(View.VISIBLE);
        Handler hl = new Handler();
        hl.postDelayed(new Runnable() {
            @Override
            public void run() {
                page += 1;
                resulUrlImageTabOne(query,page,false);
                userScroll = false;
            }
        },1000);
    }
    public void resulUrlImageTabOne(int query, int page, final Boolean isClean){
        Api.getRetrofit().create(Api.API.class)
                .searchImage("563492ad6f91700001000001bd5e05fa13044b96bb29ebd4a8a4c55f",query,page)
                .enqueue(new Callback<resulltFeatured>() {
                    @Override
                    public void onResponse(Call<resulltFeatured> call, Response<resulltFeatured> response) {
                        if(response.isSuccessful()){
                           if(isClean == true){
                               apiUrlImage.clear();
                           }
                            apiUrlImage.addAll(response.body().getPhotto());

                            ApdapterHome.notifyDataSetChanged();
                            swpiSwipeRefreshLayout.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                            progressBarCenter.setVisibility(View.GONE);
                            if(listIdImage.size() == 0){
                                Drawable drawable = getResources().getDrawable(R.mipmap.no_result);
                                frameLayout.setBackground(drawable);
                            }
                        }else{
                            swpiSwipeRefreshLayout.setRefreshing(false);
                            progressBarCenter.setVisibility(View.GONE);
                            Drawable drawable = getResources().getDrawable(R.mipmap.no_result);
                            frameLayout.setBackground(drawable);
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
}
