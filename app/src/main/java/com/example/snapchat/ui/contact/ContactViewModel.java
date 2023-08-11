package com.example.snapchat.ui.contact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.R;
import com.example.snapchat.adapter.ContactAdapter;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.data.response.EmptyResponse;
import com.example.snapchat.data.response.LoginResponse;
import com.example.snapchat.task.DeleteContactTask;
import com.example.snapchat.task.GetContactTask;
import com.example.snapchat.task.LoginTask;
import com.example.snapchat.utils.JwtManager;

import java.util.ArrayList;
import java.util.List;

public class ContactViewModel extends ViewModel {

    private List<Contact> contactList ;
    private MutableLiveData<ContactAdapter> contactAdapter;
    private MutableLiveData<Contact> onSelectedContact;
    private MutableLiveData<Integer> dialogOpened;
    public ContactViewModel() {
        contactList = new ArrayList<>();
        contactAdapter = new MutableLiveData<>();
        contactAdapter.setValue(new ContactAdapter(contactList,this));
        onSelectedContact = new MutableLiveData<>();
        dialogOpened = new MutableLiveData<>();
        dialogOpened.setValue(View.INVISIBLE);
    }

    public ContactAdapter getContactAdapter() { return contactAdapter.getValue(); }
    public MutableLiveData<Contact> getOnSelectedContact() { return onSelectedContact; }
    public MutableLiveData<Integer> getDialogOpened() { return dialogOpened; }

    @SuppressLint("StaticFieldLeak")
    public void getContactList(Context context){
        GetContactTask getContactTask = new GetContactTask(context) {
            @Override
            protected void onPostExecute(Contact[] result){
                super.onPostExecute(result);
                if(result != null) {
                    contactList.clear();
                    for(int i=0;i<result.length;i++){
                        contactList.add(result[i]);
                    }
                    contactAdapter.getValue().updateData(contactList);
                }
            }
        };
        getContactTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void selectContact(int index){
        onSelectedContact.setValue(contactList.get(index));
        openDialog();
    }
    public void closeContactDialog(){
        onSelectedContact.setValue(null);
        dialogOpened.setValue(View.INVISIBLE);
    }

    public void openDialog(){
        dialogOpened.setValue(View.VISIBLE);
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteContact(Context context){
        Bundle bundle = new Bundle();
        bundle.putString("username", onSelectedContact.getValue().username);
        Contact contactToDelete = onSelectedContact.getValue();
        DeleteContactTask deleteContactTask = new DeleteContactTask(context,bundle) {
            @Override
            protected void onPostExecute(EmptyResponse result){
                super.onPostExecute(result);
                if(result != null) {
                    contactList.remove(contactToDelete);
                    contactAdapter.getValue().updateData(contactList);
                }
            }
        };
        deleteContactTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void addContact(Contact contact){
        contactList.add(contact);
        contactAdapter.getValue().updateData(contactList);
    }
}