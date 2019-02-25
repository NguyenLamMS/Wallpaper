package com.example.lam19.wallpaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.regex.Pattern;

import am.appwise.components.ni.NoInternetDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private Button btnRegister;
    EditText txtEmail;
    EditText txtPass;
    EditText txtComfirmPass;
    ProgressBar progressBar;
    String isRegister;
    Boolean isCheckLogin =false;
    String userEmail;
    Integer idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.status_login));
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.nav_login));
        actionBar.hide();
        txtEmail = (EditText)findViewById(R.id.editRegisterEmail);
        txtPass = (EditText)findViewById(R.id.editRegisterPass);
        txtComfirmPass = (EditText)findViewById(R.id.editRegisterComfirmPass);
        progressBar = (ProgressBar)findViewById(R.id.progressBarRegister);
        btnRegister = (Button)findViewById(R.id.btnRegister) ;
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidEmail(txtEmail.getText().toString())){
                    if(txtPass.getText().toString().matches("")){
                        txtPass.setError("Email is not empty");
                    }else{
                        if(txtPass.getText().toString().equals(txtComfirmPass.getText().toString())){
                            Internet internet = new Internet();
                            if(internet.isNetworkAvailable(Register.this)){
                                resultRegister();
                            }else{
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error no connect internet", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }else{
                            txtComfirmPass.setError("Password comfirm not matching password");
                        }
                    }
                }else{
                    txtEmail.setError("Invalid email");
                }
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    public void resultLogin(String email, String pass){
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
                                Intent myIntent=new Intent(Register.this, Home.class);
                                startActivity(myIntent);
                                setResult(1);
                                finish();
                            }else{
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Login failed", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }else{
                            System.out.println(response.message());
                            Log.e(" Error ",response.message());

                        }
                    }
                    @Override
                    public void onFailure(Call<dataApiLogin> call, Throwable t) {
                        Log.e(" Error ",t.getMessage());
                        t.printStackTrace();
                    }
                });
    }
    public void resultRegister(){
        progressBar.setVisibility(View.VISIBLE);
       MyApi.getRetrofit().create(MyApi.ApiRegister.class)
               .Register(txtEmail.getText().toString(),txtPass.getText().toString())
               .enqueue(new Callback<ResponseBody>() {
                   @Override
                   public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            try {
                                isRegister = response.body().string();
                                System.out.println("regiter"+isRegister);
                                Log.e("Messenge",response.body().string());
                                if(isRegister.contains("true")){
                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Sign up success", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            resultLogin(txtEmail.getText().toString(),txtPass.getText().toString());
                                        }
                                    },1000);
                                }else{
                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Registration failed, Account already exists", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            System.out.println(response.message());
                                Log.e(" Error ",response.message());

                        }
                       progressBar.setVisibility(View.GONE);
                   }

                   @Override
                   public void onFailure(Call<ResponseBody> call, Throwable t) {
                       Log.e(" Error ",t.getMessage());
                        t.printStackTrace();
                   }
               });
    }
}
