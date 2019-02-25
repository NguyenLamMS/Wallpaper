package com.example.lam19.wallpaper;

import com.google.gson.annotations.SerializedName;

public class dataApiLogin {
    @SerializedName("login")
    Boolean resultLogin;
    @SerializedName("email")
    String email;
    @SerializedName("id")
    Integer id;

    public Boolean getResultLogin() {
        return resultLogin;
    }

    public void setResultLogin(Boolean resultLogin) {
        this.resultLogin = resultLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
