package com.example.snapchat.data.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    public int id;
    @SerializedName("username")
    public String username;
    @SerializedName("password")
    public String password;
    @SerializedName("name")
    public String name;
    @SerializedName("profile")
    public String profile;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("email")
    public String email;
}
