package com.example.snapchat.task;

import android.content.Context;
import android.os.Bundle;

import com.example.snapchat.api.LoginAPI;
import com.example.snapchat.data.response.EmptyResponse;
import com.example.snapchat.data.response.SignupResponse;
import com.example.snapchat.provider.DefaultModule;

import dagger.hilt.android.EntryPointAccessors;

public class UpdateProfileTask extends Task<EmptyResponse> {
    private Bundle parameters;
    private LoginAPI loginAPI;

    public UpdateProfileTask(Context context, Bundle parameters) {
        super();
        this.parameters = parameters;
        loginAPI = EntryPointAccessors.fromApplication(
                context, DefaultModule.ProviderEntryPoint.class).getLoginAPI();
    }

    @Override
    protected EmptyResponse doInBackground(Void... voids) {
        try {
            return loginAPI.updateUserInfo(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onException(Exception e){

    }

}
