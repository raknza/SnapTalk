package com.example.snapchat.task;

import android.content.Context;
import com.example.snapchat.api.LoginAPI;
import com.example.snapchat.data.response.AuthResponse;
import com.example.snapchat.provider.DefaultModule;
import dagger.hilt.android.EntryPointAccessors;

public class CheckAuthTask  extends Task<AuthResponse> {

    private LoginAPI loginAPI;

    public CheckAuthTask(Context context) {
        super();
        loginAPI = EntryPointAccessors.fromApplication(
                context, DefaultModule.ProviderEntryPoint.class).getLoginAPI();
    }

    @Override
    protected AuthResponse doInBackground(Void... voids) {
        try {
            return loginAPI.checkAuth();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onException(Exception e){

    }

}
