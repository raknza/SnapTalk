package com.example.snapchat.task;


import android.content.Context;
import android.os.Bundle;
import com.example.snapchat.api.ContactAPI;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.provider.DefaultModule;
import dagger.hilt.android.EntryPointAccessors;

public abstract class GetContactTask  extends Task<Contact[]>{

    private ContactAPI contactAPI;

    public GetContactTask(Context context) {
        super();
        contactAPI = EntryPointAccessors.fromApplication(
                context, DefaultModule.ProviderEntryPoint.class).getContactAPI();
    }

    @Override
    protected Contact[] doInBackground(Void... voids) {
        try {
            return contactAPI.getContactList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onException(Exception e){

    }
}
