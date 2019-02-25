package com.example.lam19.wallpaper;

import com.google.gson.annotations.SerializedName;
public class arrUrlImage {
    @SerializedName("photographer")
    String namePhoto;
    @SerializedName("src")
    srcImage src;
    @SerializedName("id")
    Integer id;

    public arrUrlImage(String namePhoto, srcImage src, Integer id) {
        this.namePhoto = namePhoto;
        this.src = src;
        this.id = id;
    }
    public arrUrlImage(){

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamePhoto() {
        return namePhoto;
    }

    public arrUrlImage(String namePhoto) {
        this.namePhoto = namePhoto;
    }

    public srcImage getSrc() {
        return src;
    }

    public void setSrc(srcImage src) {
        this.src = src;
    }

    public void setNamePhoto(String namePhoto) {
        this.namePhoto = namePhoto;
    }

    public class srcImage{
        @SerializedName("original")
        String original;
        @SerializedName("medium")
        String medium;

        public srcImage(String original, String medium) {
            this.original = original;
            this.medium = medium;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }
}



