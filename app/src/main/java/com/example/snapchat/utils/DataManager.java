package com.example.snapchat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.snapchat.data.MessageDataSource;
import com.example.snapchat.data.model.ChatPartner;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.data.model.Message;
import com.example.snapchat.data.model.User;
import com.example.snapchat.task.GetContactTask;
import com.example.snapchat.task.GetUnreceivedMessageTask;
import com.example.snapchat.task.GetUserInfoTask;
import com.example.snapchat.task.ReceivedAllMessageTask;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class DataManager {
    private static DataManager instance;

    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Contact>> contactListLiveData = new MutableLiveData<>();

    private MutableLiveData<List<ChatPartner>> chatPartners = new MutableLiveData<>();
    private Map<Integer, MutableLiveData<List<Message>>> messagesWithSender;
    private MutableLiveData<Pair<String,Message>> lastUnreceivedMessage = new MutableLiveData<>();
    MessageDataSource messageDataSource;
    private DataManager() {}

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchData(Context context){
        ReceivedAllMessageTask receivedAllMessageTask = new ReceivedAllMessageTask(context){};
        GetUnreceivedMessageTask getUnreceivedMessageTask = new GetUnreceivedMessageTask(context){
            @Override
            protected void onPostExecute(List<Message> result){
                if(result != null && result.size() > 0){
                    for(Message message:result){
                        if(message.senderId == userLiveData.getValue().id){
                            addMessageForSender(message);
                        }
                        else{
                            addMessageForSender(message.senderId,message);
                        }
                    }
                    Message lastMessage = result.get(result.size() - 1);
                    List<ChatPartner> partners = chatPartners.getValue();
                    for(int i=0;i<partners.size();i++){
                        if(partners.get(i).contact.id == lastMessage.senderId){
                            lastUnreceivedMessage.setValue(new Pair(partners.get(i).contact.username,lastMessage));
                            break;
                        }
                    }
                    receivedAllMessageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    clearUnreceivedMessage();
                }
            }
        };
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
                                partner.lastMessage.setValue(messages.getValue().get(messages.getValue().size()-1));
                            }
                        }
                        partners.add(partner);
                    }
                    chatPartners.setValue(partners);
                    getUnreceivedMessageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        };
        GetUserInfoTask getUserInfoTask = new GetUserInfoTask(context) {
            @Override
            protected void onPostExecute(User result){
                super.onPostExecute(result);
                if(result != null) {
                    userLiveData.setValue(result);
                    messageDataSource = new MessageDataSource(context);
                    messageDataSource.open();
                    messagesWithSender = messageDataSource.getMessagesWithSender();
                    getContactTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        };
        getUserInfoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<List<Contact>> getContactListLiveData() {
        return contactListLiveData;
    }

    public MutableLiveData<List<ChatPartner>> getChatPartners() { return chatPartners; }

    public MutableLiveData<Pair<String,Message>> getLastUnreceivedMessage() {return lastUnreceivedMessage;}
    public void clearUnreceivedMessage() { lastUnreceivedMessage.setValue(null);}

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

        for(ChatPartner partner: chatPartners.getValue()){
            if(partner.contact.id == message.senderId){
                partner.lastMessage.postValue(message);
                break;
            }
        }
        messageDataSource.insertMessage(message);
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

        for(ChatPartner partner: chatPartners.getValue()){
            if(partner.contact.id == message.recipientId){
                partner.lastMessage.setValue(message);
                break;
            }
        }
        messageDataSource.insertMessage(message);
    }

}