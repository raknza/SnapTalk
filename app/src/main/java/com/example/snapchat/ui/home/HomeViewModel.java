package com.example.snapchat.ui.home;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.snapchat.adapter.ChatroomAdapter;
import com.example.snapchat.adapter.ContactAdapter;
import com.example.snapchat.adapter.MessageAdapter;
import com.example.snapchat.data.model.ChatPartner;
import com.example.snapchat.data.model.Message;
import com.example.snapchat.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    MutableLiveData<ChatroomAdapter> chatroomAdapter;
    List<ChatPartner> chatPartners;
    private MutableLiveData<ChatPartner> onSelectedCharPartners;

    public HomeViewModel() {
        chatroomAdapter = new MutableLiveData<>();
        onSelectedCharPartners = new MutableLiveData<>();
    }

    public MutableLiveData<ChatroomAdapter> getChatroomAdapter() { return chatroomAdapter; }
    public MutableLiveData<ChatPartner> getOnSelectedCharPartners() { return onSelectedCharPartners; }

    public void observeChatPartners(LifecycleOwner owner) {
        DataManager.getInstance().getChatPartners().observe(owner, chatPartners -> {
            this.chatPartners = chatPartners;
            chatroomAdapter.setValue(new ChatroomAdapter(chatPartners, this));
        });
    }

    public void selectChatPartner(int index){
        onSelectedCharPartners.setValue(chatPartners.get(index));
    }

    public void closeChatroomDialog(){
        onSelectedCharPartners.setValue(null);
    }

}