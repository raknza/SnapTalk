package com.example.snapchat.task;

import android.content.Context;
import com.example.snapchat.api.ContactAPI;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.provider.DefaultModule;

import dagger.hilt.android.EntryPointAccessors;

public abstract class SearchUserTask extends Task<Contact> {
    private String username;
    private ContactAPI contactAPI;


    public SearchUserTask(Context context, String username) {
        super();
        this.username = username;
        contactAPI = EntryPointAccessors.fromApplication(
                context, DefaultModule.ProviderEntryPoint.class).getContactAPI();
    }

    @Override
    protected Contact doInBackground(Void... voids) {
        try {
            return contactAPI.searchUser(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onException(Exception e){

    }




}