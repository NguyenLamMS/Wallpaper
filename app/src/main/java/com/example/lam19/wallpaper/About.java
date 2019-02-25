package com.example.lam19.wallpaper;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class About extends AppCompatActivity {
    ImageView github ;
    ImageView facebook;
    ImageView gmail;
    ImageView instargam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");
        //create button back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorActionBar));
        github = findViewById(R.id.link_github);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_link("https://github.com/NguyenLamMS/Wallpaper.git");
            }
        });
        facebook = findViewById(R.id.link_facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_link("https://fb.com/lam1997.ln");
            }
        });
        instargam = findViewById(R.id.link_instargam);
        instargam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_link("https://www.instagram.com/lam1997.ln/");
            }
        });
        gmail = findViewById(R.id.link_gmail);
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto","lam1997.ln@gmail.com", null));
                    startActivity(Intent.createChooser(intent, "Choose an Email :"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(About.this, "No application can handle this request. Please install a web browser or messenger.",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
    public void load_link(String url){
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request. Please install a web browser or messenger.",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
