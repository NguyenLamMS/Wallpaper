package com.example.lam19.wallpaper;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.WallpaperManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.jsibbold.zoomage.ZoomageView;
import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


import am.appwise.components.ni.NoInternetDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemGridView extends AppCompatActivity {
    Button btnSetImage;
    Boolean isHidden = false;
    Boolean isHeart = false;
    MenuItem btnHearts;
    ProgressBar progressBarLoad;
    NoInternetDialog noInternetDialog;
    Intent intent;
    Bitmap imageBitmapMedium;
    ZoomageView zoomImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_grid_view);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.nav_login));
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.nav_login));
        progressBarLoad = findViewById(R.id.progressBarItemGrid);
        progressBarLoad.setVisibility(View.VISIBLE);
        zoomImage = findViewById(R.id.imageView);
        btnSetImage = (Button) findViewById(R.id.btnSetImage);
        //check item đã có trong favarite hay chưa
        checkFavorite();

        noInternetDialog = new NoInternetDialog.Builder(this).setButtonColor(Color.parseColor("#fe3f80")).setDialogRadius((float) 50).setCancelable(true).build();
        intent = getIntent();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    progressBarLoad.setVisibility(View.VISIBLE);
                    imageBitmapMedium = Glide.
                            with(ItemGridView.this).
                            asBitmap().
                            load(intent.getStringExtra("image_portrait")).
                            into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).
                            get();
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           zoomImage.setImageBitmap(imageBitmapMedium);
                           progressBarLoad.setVisibility(View.GONE);
                       }
                   });
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Toast.makeText(ItemGridView.this, "Unknown error", Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Toast.makeText(ItemGridView.this, "Unknown error", Toast.LENGTH_LONG).show();
                }
            }
        }).start();
        btnSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog(false);
            }
        });
        //create button back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //tranparent actionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Click hidden actionBar and statusBar and navBar
//        zoomImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isHidden == true) {
//                    ActionBar actionBar = getSupportActionBar();
//                    actionBar.hide();
//                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    isHidden = false;
//                } else {
//                    ActionBar actionBar = getSupportActionBar();
//                    actionBar.show();
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    isHidden = true;
//                }
//            }
//        });
        setTitle(intent.getStringExtra("name_image"));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(noInternetDialog != null){
            try {
                noInternetDialog.onDestroy();
                noInternetDialog = null;
            }catch (Exception ex){
                System.out.println("Error" + ex.getMessage());
            }
        }
    }
    public void showMyDialog(final Boolean isDowload){
        final String[] fonts = {"Original","Large","Portrait"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemGridView.this);
        builder.setTitle("Choose image quality");
        builder.setItems(fonts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Original".equals(fonts[which])){
                        if(isDowload){
                            dowloadImageAndSave(intent.getStringExtra("image_original"));
                        }else{
                            setWallpaper(intent.getStringExtra("image_original"));
                        }
                }
                else if ("Large".equals(fonts[which])){
                        if(isDowload){
                            dowloadImageAndSave(intent.getStringExtra("image_large"));
                        }else{
                            setWallpaper(intent.getStringExtra("image_large"));
                        }
                }
                else if ("Portrait".equals(fonts[which])){
                        if(isDowload){
                            dowloadImageAndSave(intent.getStringExtra("image_portrait"));
                        }else{
                            setWallpaper(intent.getStringExtra("image_portrait"));
                        }
                }
            }
        });
        builder.show();
    }

    public void setWallpaper( final String url){
        progressBarLoad.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Glide.with(ItemGridView.this).asBitmap().load(url).into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).get();
                    WallpaperManager.getInstance(getApplicationContext()).setBitmap(bitmap);
                } catch (IOException e) {
                    Toast.makeText(ItemGridView.this, "Unknown error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Toast.makeText(ItemGridView.this, "Unknown error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Toast.makeText(ItemGridView.this, "Unknown error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarLoad.setVisibility(View.GONE);
                        Toast.makeText(ItemGridView.this, "Wallpaper update", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    public void dowloadImageAndSave(String url) {
        if (checkPermission()) {
            new DownloadFile().execute(url);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ItemGridView.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(ItemGridView.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(ItemGridView.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                dowloadImageAndSave(url);
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ItemGridView.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private String saveImage(Bitmap image) {
        Random rand = new Random();
        String savedImagePath = null;
        String imageFileName = "Wallpaper_" + UUID.randomUUID().toString() + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/Wallpaper");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_grid_view, menu);
        btnHearts = menu.findItem(R.id.btnHeart);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.btnDowload) {
            showMyDialog(true);
        }
        if (item.getItemId() == R.id.btnHeart) {
            if (isHeart == false) {
                btnHearts.setIcon(R.drawable.icon_heart_red);
                isHeart = true;
                addFavorite();

            } else {
                btnHearts.setIcon(R.drawable.icon_heart_white);
                isHeart = false;
                removeFavorite();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void addFavorite() {
        Intent intent = getIntent();
        Integer id_image = intent.getIntExtra("id_image", 0);
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        Integer id_user = sp.getInt("id", -1);
        String image_medium = intent.getStringExtra("image_medium");
        String name_image = intent.getStringExtra("name_image");
        String image_original = intent.getStringExtra("image_original");
        String image_large = intent.getStringExtra("image_large");
        String image_portrait = intent.getStringExtra("image_portrait");
        MyApi.getRetrofit().create(MyApi.ApiFavorite.class)
                .Favorite(id_image, id_user,image_medium,name_image,image_original,image_large,image_portrait)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ItemGridView.this, " Add to favorite", Toast.LENGTH_SHORT).show();
                            Log.e("add favorite", response.body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    public void removeFavorite() {
        Intent intent = getIntent();
        Integer id_image = intent.getIntExtra("id_image", 0);
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        Integer id_user = sp.getInt("id", -1);

        MyApi.getRetrofit().create(MyApi.ApiRemoveFavorite.class)
                .RemoveFavorite(id_image, id_user)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ItemGridView.this, " Remove favorite", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }
    public void checkFavorite() {
        Intent intent = getIntent();
        Integer id_image = intent.getIntExtra("id_image", 0);
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        Integer id_user = sp.getInt("id", -1);

        MyApi.getRetrofit().create(MyApi.ApiCheckFavorite.class)
                .CheckFavorite(id_image, id_user)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                if (response.body().string().contains("true")) {
                                    btnHearts.setIcon(R.drawable.icon_heart_red);
                                    isHeart = true;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class DownloadFile extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(ItemGridView.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                Bitmap bitmap = Glide.with(ItemGridView.this).asBitmap().load(f_url[0]).into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).get();
                saveImage(bitmap);
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                }
                input.close();
                return "Image Saved ";

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }

    }
}
