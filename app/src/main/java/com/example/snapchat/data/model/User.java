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

    public User(User user){
        this.id = user.id;
        this.username = user.username;
        this.password = user.password;
        this.name = user.name;
        this.profile = user.profile;
        this.avatar = user.avatar;
        this.email = user.email;
    }
}
