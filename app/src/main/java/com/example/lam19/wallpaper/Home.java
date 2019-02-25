package com.example.lam19.wallpaper;

import android.content.ActivityNotFoundException;

import android.content.Intent;
import android.content.SharedPreferences;


import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;

import am.appwise.components.ni.NoInternetDialog;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    NoInternetDialog noInternetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorActionBar));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewpage = (ViewPager) findViewById(R.id.viewPage);
        viewpage.setAdapter(new MyApdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewpage);
        noInternetDialog = new NoInternetDialog.Builder(this).setButtonColor(Color.parseColor("#fe3f80")).setDialogRadius((float) 50).setCancelable(true).build();

//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //set email nav
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        String email = sp1.getString("email", null);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userEmail);
        navUsername.setText(email);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuInflater menuInflater = getMenuInflater();
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent myIntent = new Intent(Home.this, Search.class);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_heart) {
            Intent intent = new Intent(Home.this,favorite.class);
//            intent.putExtra("image",listImageURLs.get(i));
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(Home.this, About.class);
            startActivity(intent);
        } else if (id == R.id.nav_feedback) {
                try {
                    Uri webpage = Uri.parse("https://www.messenger.com/t/lam1997.ln");
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "No application can handle this request. Please install a web browser or messenger.",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


        } else if (id == R.id.nav_logout) {
            SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putInt("id",-1);
            Ed.putString("email",null);
            Ed.commit();
            Intent myIntent=new Intent(Home.this, Login.class);
            startActivity(myIntent);
            finish();

        } else if (id == R.id.nav_share) {
            final String appPackageName = this.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            this.startActivity(sendIntent);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void checkInternet(){

    }
}
