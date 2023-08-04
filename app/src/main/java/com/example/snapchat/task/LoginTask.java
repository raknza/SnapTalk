package com.example.snapchat.task;


import android.content.Context;
import android.os.Bundle;

import com.example.snapchat.api.LoginAPI;
import com.example.snapchat.provider.DefaultModule;

import dagger.hilt.android.EntryPointAccessors;
import okhttp3.ResponseBody;

public abstract class LoginTask extends Task<ResponseBody> {

    private Bundle parameters;
    private String jwtToken;
    private LoginAPI loginAPI;

    public LoginTask(Context context, Bundle parameters) {
        super(context);
        this.parameters = parameters;
        loginAPI = EntryPointAccessors.fromApplication(
                context, DefaultModule.ProviderEntryPoint.class).getLoginAPI();
    }

    @Override
    protected ResponseBody doInBackground(Void... voids) {
        try {
            return loginAPI.login(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}