package com.example.snapchat.api;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.snapchat.service.LoginService;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Response;

@Singleton
public class LoginAPI {

    @NonNull
    private final LoginService loginService;

    /*@NonNull
    private final Gson gson;*/

    @Inject
    public LoginAPI(@NonNull LoginService loginService) {
        this.loginService = loginService;
    }


    @NonNull
    public ResponseBody login(Bundle parameters) throws Exception {
        final Map<String, String> parameterMap = new HashMap<>();
        for (String key : parameters.keySet()) {
            parameterMap.put(key, parameters.getString(key));
        }
        Response<ResponseBody> response = loginService.login(parameterMap).execute();
        if (response.isSuccessful()) {
            Log.d("login", "success!!!" + response.body().string());
        }
        else{
            throw new Exception(response.message());
        }

        return response.body();
    }






    @NonNull
    private void register(Bundle parameters) throws Exception {
        final Map<String, String> parameterMap = new HashMap<>();
        for (String key : parameters.keySet()) {
            parameterMap.put(key, parameters.getString(key));
        }
        final Response<ResponseBody> response = loginService.register(parameterMap).execute();
        if (response.isSuccessful()) {
            Log.d("register", "success!!!");
        }
    }



}
