package com.example.snapchat.task;

import android.content.Context;
import android.os.Bundle;

import com.example.snapchat.api.LoginAPI;
import com.example.snapchat.data.response.SignupResponse;
import com.example.snapchat.provider.DefaultModule;

import dagger.hilt.android.EntryPointAccessors;

public abstract class SignupTask extends Task<SignupResponse> {
    private Bundle parameters;
    private LoginAPI loginAPI;


    public SignupTask(Context context, Bundle parameters) {
        super();
        this.parameters = parameters;
        loginAPI = EntryPointAccessors.fromApplication(
                context, DefaultModule.ProviderEntryPoint.class).getLoginAPI();
    }

    @Override
    protected SignupResponse doInBackground(Void... voids) {
        try {
            return loginAPI.signup(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onException(Exception e){

    }




}