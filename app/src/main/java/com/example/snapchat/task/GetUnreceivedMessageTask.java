package com.example.snapchat.task;

import android.content.Context;
import com.example.snapchat.api.MessageAPI;
import com.example.snapchat.data.model.Message;
import com.example.snapchat.provider.DefaultModule;
import java.util.List;
import dagger.hilt.android.EntryPointAccessors;

public class GetUnreceivedMessageTask extends Task<List<Message>>{

    private MessageAPI messageAPI;

    public GetUnreceivedMessageTask(Context context) {
        super();
        messageAPI = EntryPointAccessors.fromApplication(
                context, DefaultModule.ProviderEntryPoint.class).getMessagePI();
    }

    @Override
    protected List<Message> doInBackground(Void... voids) {
        try {
            return messageAPI.getUnreceivedMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onException(Exception e){

    }
}
