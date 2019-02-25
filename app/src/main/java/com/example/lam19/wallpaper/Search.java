package com.example.lam19.wallpaper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search<onCreateOptionsMenu> extends AppCompatActivity {
    private GridView myGridView;
    ArrayList<String> listImageURLsMedium = new ArrayList<>();
    ArrayList<String> listImageURLsOriginal = new ArrayList<>();
    ArrayList<Integer> listId = new ArrayList<>();
    ArrayList<String> listName = new ArrayList<>();
    private  ArrayList<arrUrlImage> apiUrlImage = new ArrayList<>();
    Integer[] totalLike = {20,30,40,50,34,56};
    Integer[] totalDownload = {50,33,34,67,76,85};
    Integer countTotal = 0;
    CustomAdapterSearch customAdapter;
    ProgressBar progressBar ;
    int page = 1;
    int per_page = 15;
    Boolean userScroll = false;
    Internet internet;
    NoInternetDialog noInternetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorActionBar));
        myGridView = (GridView) findViewById(R.id.gridviewSearch);
        progressBar = (ProgressBar)  findViewById(R.id.progressBarSearch);
        customAdapter = new CustomAdapterSearch(Search.this,R.layout.custom_gridview,listImageURLsMedium);
        myGridView.setAdapter(customAdapter);
        noInternetDialog = new NoInternetDialog.Builder(this).setButtonColor(Color.parseColor("#fe3f80")).setDialogRadius((float) 50).setCancelable(true).build();
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Search.this,ItemGridView.class);
                intent.putExtra("image",listImageURLsOriginal.get(position));
                intent.putExtra("id_image",listId.get(position));
                startActivity(intent);
            }
        });
        //check internet
        internet = new Internet();
        if(internet.isNetworkAvailable(this)){
            progressBar.setVisibility(View.VISIBLE);
            resulUrlImageSearch(per_page,page,true);
        }else{
            progressBar.setVisibility(View.GONE);
            FrameLayout frameLayout = findViewById(R.id.frameLayoutSearch);
            Drawable drawable = getResources().getDrawable(R.mipmap.no_internet);
            frameLayout.setBackground(drawable);
        }
        myGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                userScroll = true;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(userScroll && firstVisibleItem + visibleItemCount == totalItemCount){
                    userScroll = false;
                    page += 1;
                    resulUrlImageSearch(per_page,page,false);
                    System.out.println("page :" + page);
                }
            }

        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    public void resulUrlImageSearch(Integer per_page, Integer page, final Boolean isClean){
        Intent intent = getIntent();
        Api.getRetrofit().create(Api.APISearch.class)
                .searchImageQuery("563492ad6f91700001000001bd5e05fa13044b96bb29ebd4a8a4c55f",intent.getStringExtra("valueSearch") ,per_page,page)
                .enqueue(new Callback<resulltFeatured>() {
                    @Override
                    public void onResponse(Call<resulltFeatured> call, Response<resulltFeatured> response) {
                        if(response.isSuccessful()){
                                apiUrlImage.clear();
                                if(isClean == true){
                                    listImageURLsOriginal.clear();
                                    listImageURLsMedium.clear();
                                    listId.clear();
                                    listName.clear();
                                }
                            apiUrlImage.addAll(response.body().getPhotto());
                            for (arrUrlImage  a: apiUrlImage) {
                                listImageURLsMedium.add(a.src.medium);
                                listImageURLsOriginal.add(a.src.original);
                                listId.add(a.id);
                                listName.add(a.namePhoto);
                            }
                            progressBar.setVisibility(View.GONE);
                            if(listId.size() == 0){
                                FrameLayout frameLayout = findViewById(R.id.frameLayoutSearch);
                                Drawable drawable = getResources().getDrawable(R.mipmap.no_result);
                                frameLayout.setBackground(drawable);
                            }
                            customAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }else{
                            progressBar.setVisibility(View.GONE);
                            FrameLayout frameLayout = findViewById(R.id.frameLayoutSearch);
                            Drawable drawable = getResources().getDrawable(R.mipmap.no_result);
                            frameLayout.setBackground(drawable);
                            Log.e(" Error ",response.message());

                        }
                    }

                    @Override
                    public void onFailure(Call<resulltFeatured> call, Throwable t) {
                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        getMenuInflater().inflate(R.menu.home, menu);
        MenuInflater menuInflater = getMenuInflater();
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setQuery(intent.getStringExtra("valueSearch"),false);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent myIntent = new Intent(Search.this, Search.class);
                myIntent.putExtra("valueSearch",query);
                startActivity(myIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



    private class CustomAdapterSearch extends BaseAdapter {
        private Context context;
        private int layout;
        private List<String> listImageURLs;

        public CustomAdapterSearch(Context context, int layout, List<String> images) {
            this.context = context;
            this.layout = layout;
            this.listImageURLs = images;
        }

        @Override
        public int getCount() {
            return listImageURLs.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder ;
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView == null){
                convertView = inflater.inflate(layout,null);
            }else{
                viewHolder = (ViewHolder)  convertView.getTag();
            }
            viewHolder.txtLike = convertView.findViewById(R.id.totalLike);
            viewHolder.txtDownload = convertView.findViewById(R.id.totalDownload);
            viewHolder.txtLike.setText(totalLike[countTotal].toString());
            viewHolder.txtDownload.setText(totalDownload[countTotal].toString());
            if(countTotal > 4){
                countTotal = 0;
            }else{
                countTotal += 1;
            }
            viewHolder.name = convertView.findViewById(R.id.txtNameImage);
            viewHolder.name.setText(listName.get(position));
            viewHolder.image = (ImageView) convertView.findViewById(R.id.images);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.containerGrid);
            convertView.setTag(viewHolder);
            viewHolder.linearLayout.setBackgroundColor(getRandomColor());
            //load image
            Glide.with(context)
                    .load(listImageURLs.get(position))
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.loading)
                            .centerCrop()
                            .override(200,800)
                            .dontAnimate()
                            .dontTransform())
                    .into(viewHolder.image);
            return convertView;
        }
        public int getRandomColor(){
            Random rnd = new Random();
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        }
        public class ViewHolder{
            ImageView image;
            LinearLayout linearLayout;
            TextView name;
            TextView txtLike;
            TextView txtDownload;
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
}
