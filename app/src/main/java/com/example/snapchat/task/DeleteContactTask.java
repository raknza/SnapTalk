package com.example.snapchat.task;

import android.content.Context;
import android.os.Bundle;

import com.example.snapchat.api.ContactAPI;
import com.example.snapchat.data.response.EmptyResponse;
import com.example.snapchat.provider.DefaultModule;

import dagger.hilt.android.EntryPointAccessors;

public class DeleteContactTask extends Task<EmptyResponse> {

    private ContactAPI contactAPI;
    private Bundle parameters;

    public DeleteContactTask(Context context, Bundle parameters) {
        super();
        this.parameters = parameters;
        contactAPI = EntryPointAccessors.fromApplication(
                context, DefaultModule.ProviderEntryPoint.class).getContactAPI();
    }

    @Override
    protected EmptyResponse doInBackground(Void... voids) {
        try {
            return contactAPI.deleteContact(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onException(Exception e){

    }
}
