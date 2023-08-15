package com.example.snapchat.data.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ChatPartner {

    public Contact contact;
    public MutableLiveData<Message> lastMessage = new MutableLiveData<>();
    public LiveData<Message> getLastMessage() {
        return lastMessage;
    }
}
