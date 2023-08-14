package com.example.snapchat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.snapchat.data.model.ChatPartner;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.data.model.Message;
import com.example.snapchat.data.model.User;
import com.example.snapchat.task.GetContactTask;
import com.example.snapchat.task.GetUserInfoTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static DataManager instance;

    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Contact>> contactListLiveData = new MutableLiveData<>();

    private MutableLiveData<List<ChatPartner>> chatPartners = new MutableLiveData<>();
    private Map<Integer, MutableLiveData<List<Message>>> messagesWithSender = new HashMap<>();
    private DataManager() {
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchData(Context context){
        GetUserInfoTask getUserInfoTask = new GetUserInfoTask(context) {
            @Override
            protected void onPostExecute(User result){
                super.onPostExecute(result);
                if(result != null) {
                    userLiveData.setValue(result);
                }
            }
        };
        getUserInfoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        GetContactTask getContactTask = new GetContactTask(context) {
            @Override
            protected void onPostExecute(Contact[] result){
                super.onPostExecute(result);
                if(result != null) {
                    List<Contact> contacts = Arrays.asList(result);
                    List<ChatPartner> partners = new ArrayList<>();
                    contactListLiveData.setValue(contacts);
                    for (Contact contact : contacts){
                        ChatPartner partner = new ChatPartner();
                        partner.contact = contact;
                        MutableLiveData<List<Message>> messages = messagesWithSender.get(contact.id);
                        if( messages != null && messages.getValue() != null){
                            if( messages.getValue().size() != 0){
                                partner.lastMessage = messages.getValue().get(messages.getValue().size()-1);
                            }
                        }
                        partners.add(partner);
                    }
                    chatPartners.setValue(partners);
                }
            }
        };
        getContactTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<List<Contact>> getContactListLiveData() {
        return contactListLiveData;
    }

    public MutableLiveData<List<ChatPartner>> getChatPartners() { return chatPartners; }

    public void updateUser(User user) {
        userLiveData.postValue(user);
    }

    public void updateContactList(List<Contact> contactList) {
        contactListLiveData.postValue(contactList);
    }

    public void removeContact(Contact beRemovingContact){
        List<Contact> newContactList = new ArrayList<>(contactListLiveData.getValue());
        newContactList.removeIf(contact -> contact.id == beRemovingContact.id);
        contactListLiveData.setValue(newContactList);
    }
    public void addContact(Contact contact){
        List<Contact> newContactList = new ArrayList<>(contactListLiveData.getValue());
        newContactList.add(contact);
        contactListLiveData.setValue(newContactList);
    }

    public LiveData<List<Message>> getMessagesWithPartner(int partnerId) {
        if (!messagesWithSender.containsKey(partnerId)) {
            messagesWithSender.put(partnerId, new MutableLiveData<>(new ArrayList<>()));
        }
        return messagesWithSender.get(partnerId);
    }

    public void addMessageForSender(int senderId, Message message) {
        MutableLiveData<List<Message>> senderMessages = messagesWithSender.get(senderId);
        if (senderMessages == null) {
            senderMessages = new MutableLiveData<>(new ArrayList<>());
            messagesWithSender.put(senderId, senderMessages);
        }

        List<Message> currentMessages = senderMessages.getValue();
        if (currentMessages != null) {
            currentMessages.add(message);
            senderMessages.postValue(currentMessages);
        }
    }


    public void updateMessagesForSender(int senderId, List<Message> messages) {
        if (messagesWithSender.containsKey(senderId)) {
            messagesWithSender.get(senderId).postValue(messages);
        }
    }

    public void addMessageForSender(Message message) {
        int recipientId = message.recipientId;
        if (!messagesWithSender.containsKey(recipientId)) {
            messagesWithSender.put(recipientId, new MutableLiveData<>(new ArrayList<>()));
        }

        MutableLiveData<List<Message>> senderMessages = messagesWithSender.get(recipientId);
        List<Message> currentMessages = senderMessages.getValue();

        if (currentMessages == null) {
            currentMessages = new ArrayList<>();
        }

        currentMessages.add(message);
        senderMessages.setValue(currentMessages);
    }

}