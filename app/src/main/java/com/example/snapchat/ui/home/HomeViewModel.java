package com.example.snapchat.ui.home;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
    private MutableLiveData<ChatPartner> onSelectedChatPartners;

    public HomeViewModel() {
        chatroomAdapter = new MutableLiveData<>();
        onSelectedChatPartners = new MutableLiveData<>();
    }

    public MutableLiveData<ChatroomAdapter> getChatroomAdapter() { return chatroomAdapter; }
    public MutableLiveData<ChatPartner> getOnSelectedCharPartners() { return onSelectedChatPartners; }

    public void observeChatPartners(LifecycleOwner owner) {
        DataManager.getInstance().getChatPartners().observe(owner, chatPartners -> {
            this.chatPartners = chatPartners;
            chatroomAdapter.setValue(new ChatroomAdapter(chatPartners, this));
            for(ChatPartner partner:chatPartners){
                observeLastMessage(owner,partner);
            }
        });
    }
    private void observeLastMessage(LifecycleOwner owner, ChatPartner chatPartner) {
        chatPartner.lastMessage.observe(owner, message -> chatroomAdapter.getValue().updateData());
    }

    public void selectChatPartner(int index){
        onSelectedChatPartners.setValue(chatPartners.get(index));
    }

    public void selectChatPartner(String username){
        for(int i=0;i<chatPartners.size();i++){
            if(username.equals(chatPartners.get(i).contact.username)){
                onSelectedChatPartners.setValue(chatPartners.get(i));
                break;
            }
        }
    }

    public void closeChatroomDialog(){
        onSelectedChatPartners.setValue(null);
    }

}