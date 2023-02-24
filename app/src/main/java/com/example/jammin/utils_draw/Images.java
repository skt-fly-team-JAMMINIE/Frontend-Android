package com.example.jammin.utils_draw;

import com.google.gson.annotations.SerializedName;

public class Images {
    @SerializedName("house_img")
    private String house_img;

    @SerializedName("tree_img")
    private String tree_img;

    @SerializedName("person_img")
    private String person_img;

    public Images(String house_img, String tree_img, String person_img) {
        this.house_img = house_img;
        this.tree_img = tree_img;
        this.person_img = person_img;
    }
}
