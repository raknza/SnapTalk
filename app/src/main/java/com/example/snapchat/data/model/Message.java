package com.example.snapchat.data.model;

import com.example.snapchat.adapter.CustomDateAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Message {
    @SerializedName("id")
    public int id;
    @SerializedName("senderId")
    public int senderId;
    @SerializedName("recipientId")
    public int recipientId;
    @SerializedName("content")
    public String content;
    @SerializedName("messageType")
    public MessageType messageType;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("isRead")
    public boolean isRead;
    @SerializedName("isReceived")
    public boolean isReceived;
    public enum MessageType {
        TEXT,
        IMAGE,
        FILE,
        LINK
    }
}


