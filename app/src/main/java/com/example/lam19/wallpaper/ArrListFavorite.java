package com.example.lam19.wallpaper;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArrListFavorite {
    @SerializedName("favorite")
    ArrayList<Integer> listFavorite;
    @SerializedName("image_medium")
    ArrayList<String> listImageMedium;
    @SerializedName("name_image")
    ArrayList<String> listName;
    @SerializedName("image_original")
    ArrayList<String> listImageOriginal;
    public ArrayList<Integer> getListFavorite() {
        return listFavorite;
    }

    public void setListFavorite(ArrayList<Integer> listFavorite) {
        this.listFavorite = listFavorite;
    }

    public ArrayList<String> getListImageMedium() {
        return listImageMedium;
    }

    public void setListImageMedium(ArrayList<String> listImageMedium) {
        this.listImageMedium = listImageMedium;
    }

    public ArrayList<String> getListName() {
        return listName;
    }

    public void setListName(ArrayList<String> listName) {
        this.listName = listName;
    }

    public ArrayList<String> getListImageOriginal() {
        return listImageOriginal;
    }

    public void setListImageOriginal(ArrayList<String> listImageOriginal) {
        this.listImageOriginal = listImageOriginal;
    }
}
