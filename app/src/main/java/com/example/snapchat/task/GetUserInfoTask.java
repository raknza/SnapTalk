package com.example.snapchat.task;

import android.content.Context;

import com.example.snapchat.api.LoginAPI;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.data.model.User;
import com.example.snapchat.data.response.AuthResponse;
import com.example.snapchat.provider.DefaultModule;

import dagger.hilt.android.EntryPointAccessors;

public class GetUserInfoTask extends Task<User> {

    private LoginAPI loginAPI;

    public GetUserInfoTask(Context context) {
        super();
        loginAPI = EntryPointAccessors.fromApplication(
                context, DefaultModule.ProviderEntryPoint.class).getLoginAPI();
    }

    @Override
    protected User doInBackground(Void... voids) {
        try {
            return loginAPI.getInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onException(Exception e){

    }
}
