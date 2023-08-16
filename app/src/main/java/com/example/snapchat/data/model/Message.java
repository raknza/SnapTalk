package com.example.snapchat.data.model;

import com.example.snapchat.adapter.CustomDateAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        @SerializedName("0")
        TEXT,
        @SerializedName("1")
        IMAGE,
        @SerializedName("2")
        FILE,
        @SerializedName("3")
        LINK
    }

    public String getTime() {
        return convertDateFormat(timestamp);
    }
    static String inputFormat = "MMM dd, yyyy HH:mm:ss";
    static String outputFormat = "HH:mm";
    public static String convertDateFormat(String inputDate) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat, Locale.TAIWAN);
            Date date = inputDateFormat.parse(inputDate);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat, Locale.TAIWAN);
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}


