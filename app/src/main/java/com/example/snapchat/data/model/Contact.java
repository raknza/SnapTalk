package com.example.snapchat.data.model;

import com.google.gson.annotations.SerializedName;

public class Contact {

    @SerializedName("id")
    public int id;
    @SerializedName("username")
    public String username;
    @SerializedName("name")
    public String name;
    @SerializedName("profile")
    public String profile;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("email")
    public String email;
}
