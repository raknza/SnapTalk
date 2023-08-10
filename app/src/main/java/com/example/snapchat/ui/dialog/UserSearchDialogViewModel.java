package com.example.snapchat.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.R;
import com.example.snapchat.adapter.ContactAdapter;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.data.response.EmptyResponse;
import com.example.snapchat.task.AddContactTask;
import com.example.snapchat.task.SearchUserTask;
import java.util.ArrayList;
import java.util.List;

public class UserSearchDialogViewModel extends ViewModel {

    private List<Contact> contactList ;
    private MutableLiveData<ContactAdapter> contactAdapter;
    public final MutableLiveData<String> searchingUsername ;
    private MutableLiveData<Integer> addButtonVisiblity;
    private MutableLiveData<String> searchResult;
    private MutableLiveData<Boolean> addContactSuccess;

    public UserSearchDialogViewModel() {
        contactList = new ArrayList<>();
        contactAdapter = new MutableLiveData<>();
        this.searchingUsername = new MutableLiveData<>();
        addButtonVisiblity = new MutableLiveData<>();
        contactAdapter.setValue(new ContactAdapter(contactList,this));
        searchResult = new MutableLiveData<>();
        addContactSuccess = new MutableLiveData<>();
        addButtonVisiblity.setValue(View.INVISIBLE);
    }

    public MutableLiveData<String> getSearchingUsername() { return searchingUsername; }
    public ContactAdapter getContactAdapter() { return contactAdapter.getValue(); }

    public MutableLiveData<Integer> getAddButtonVisiblity() { return addButtonVisiblity; }
    public MutableLiveData<String> getSearchResult()  {return searchResult; }
    public MutableLiveData<Boolean> getAddContactSuccess() { return addContactSuccess; }
    @SuppressLint("StaticFieldLeak")
    public void searchUser(Context context){
        SearchUserTask searchUserTask = new SearchUserTask(context,searchingUsername.getValue()) {
            @Override
            protected void onPostExecute(Contact result){
                super.onPostExecute(result);
                contactList.clear();
                if(result != null) {
                    contactList.add(result);
                    addButtonVisiblity.setValue(View.VISIBLE);
                    searchResult.setValue("");
                }
                else {
                    addButtonVisiblity.setValue(View.INVISIBLE);
                    searchResult.setValue(context.getString(R.string.search_failed));
                }
                contactAdapter.getValue().updateData(contactList);
            }
        };
        searchUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    public void addContact(Context context){
        Bundle parameters = new Bundle();
        parameters.putString("username",searchingUsername.getValue());
        AddContactTask addContactTask = new AddContactTask(context,parameters) {
            @Override
            protected void onPostExecute(EmptyResponse result){
                super.onPostExecute(result);
                contactList.clear();
                addButtonVisiblity.setValue(View.INVISIBLE);
                // add contact success
                if(result != null) {
                    searchResult.setValue(context.getString(R.string.add_contact_success));
                    addContactSuccess.setValue(true);
                }
                else
                    searchResult.setValue(context.getString(R.string.add_contact_failed));

                contactAdapter.getValue().updateData(contactList);
            }
        };
        addContactTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
