package com.example.lam19.wallpaper;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class resulltFeatured {
    @SerializedName("per_page")
    String name;
    @SerializedName("photos")
    List<arrUrlImage> photto;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public resulltFeatured(String name, List<arrUrlImage> photto) {
        this.name = name;
        this.photto = photto;
    }

    public List<arrUrlImage> getPhotto() {
        return photto;
    }

    public void setPhotto(List<arrUrlImage> photto) {
        this.photto = photto;
    }
}
