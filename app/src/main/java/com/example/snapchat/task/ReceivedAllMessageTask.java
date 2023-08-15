package com.example.snapchat.task;

import android.content.Context;
import com.example.snapchat.api.MessageAPI;
import com.example.snapchat.data.response.EmptyResponse;
import com.example.snapchat.provider.DefaultModule;
import dagger.hilt.android.EntryPointAccessors;

public class ReceivedAllMessageTask extends Task<EmptyResponse>{

    private MessageAPI messageAPI;

    public ReceivedAllMessageTask(Context context) {
        super();
        messageAPI = EntryPointAccessors.fromApplication(
                context, DefaultModule.ProviderEntryPoint.class).getMessagePI();
    }

    @Override
    protected EmptyResponse doInBackground(Void... voids) {
        try {
            return messageAPI.receivedMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onException(Exception e){

    }
}