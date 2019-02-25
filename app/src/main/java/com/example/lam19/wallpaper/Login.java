package com.example.lam19.wallpaper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.regex.Pattern;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Login extends AppCompatActivity {
    private Button btnRegister;
    private Button btnLogin;
    EditText txtEmail;
    EditText txtPass;
    Boolean isCheckLogin =false;
    String userEmail;
    Integer idUser;
    ProgressBar progressBar;
    Internet internet;
    NoInternetDialog noInternetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.status_login));
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.nav_login));
        noInternetDialog = new NoInternetDialog.Builder(this).setButtonColor(Color.parseColor("#fe3f80")).setDialogRadius((float) 50).setCancelable(true).build();
        // check status login
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        Integer id =sp1.getInt("id", -1);
        String email = sp1.getString("email", null);

        if(email != null && id != -1){
            Intent myIntent=new Intent(Login.this, Home.class);
            startActivity(myIntent);
            finish();
        }
        // end check status login
        setContentView(R.layout.activity_login);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPass = (EditText)findViewById(R.id.txtPass);
        progressBar = (ProgressBar)findViewById(R.id.progressBarLogin);
        txtEmail.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent(Login.this, Register.class);
                startActivityForResult(myIntent,0);

            }
        });
        internet = new Internet();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidEmail(txtEmail.getText().toString())){
                    if(txtPass.getText().toString().matches("")){
                        txtPass.setError("Email is not empty");
                    }else{
                        //check internet
                        if(internet.isNetworkAvailable(Login.this)){
                            resultLogin(txtEmail.getText().toString(), txtPass.getText().toString());
                        }else{
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error no connect internet", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }else{
                    txtEmail.setError("Invalid email");
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
    // finish login if register success
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == 1){
            finish();
        }
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    public void resultLogin(String email, String pass){
        progressBar.setVisibility(View.VISIBLE);
        MyApi.getRetrofit().create(MyApi.ApiLogin.class)
                .Login(email,pass)
                .enqueue(new Callback<dataApiLogin>() {
                    @Override
                    public void onResponse(Call<dataApiLogin> call, Response<dataApiLogin> response) {
                        if(response.isSuccessful()){
                           isCheckLogin = response.body().getResultLogin();
                           userEmail = response.body().getEmail();
                           idUser = response.body().getId();
                           if(isCheckLogin == true){
                               SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                               SharedPreferences.Editor Ed=sp.edit();
                               Ed.putInt("id",idUser);
                               Ed.putString("email",userEmail);
                               Ed.commit();
                               Intent myIntent=new Intent(Login.this, Home.class);
                               startActivity(myIntent);
                               finish();
                           }else{
                               Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Login failed, Wrong account or password", Snackbar.LENGTH_LONG);
                               snackbar.show();
                           }
                        }else{
                            System.out.println(response.message());
                            Log.e(" Error ",response.message());

                        }
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onFailure(Call<dataApiLogin> call, Throwable t) {
                        Log.e(" Error ",t.getMessage());
                        t.printStackTrace();
                    }
                });
    }
}

