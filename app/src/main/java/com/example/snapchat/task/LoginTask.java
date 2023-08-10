package com.example.snapchat.task;


import android.content.Context;
import android.os.Bundle;
import com.example.snapchat.api.LoginAPI;
import com.example.snapchat.data.response.LoginResponse;
import com.example.snapchat.provider.DefaultModule;
import dagger.hilt.android.EntryPointAccessors;

public abstract class LoginTask extends Task<LoginResponse> {
    private Bundle parameters;
    private LoginAPI loginAPI;


    public LoginTask(Context context, Bundle parameters) {
        super();
        this.parameters = parameters;
        loginAPI = EntryPointAccessors.fromApplication(
                context, DefaultModule.ProviderEntryPoint.class).getLoginAPI();
    }

    @Override
    protected LoginResponse doInBackground(Void... voids) {
        try {
            return loginAPI.login(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onException(Exception e){

    }




}