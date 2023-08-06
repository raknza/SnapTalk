package com.example.snapchat.data.response;
import com.google.gson.annotations.SerializedName;

public class SignupResponse{
    @SerializedName("message")
    public String message;
    @SerializedName("token")
    public String token;
}
