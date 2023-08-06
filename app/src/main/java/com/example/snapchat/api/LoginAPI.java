package com.example.snapchat.api;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.snapchat.data.response.LoginResponse;
import com.example.snapchat.data.response.SignupResponse;
import com.example.snapchat.service.LoginService;
import com.example.snapchat.utils.HttpStatus;
import com.example.snapchat.utils.JwtManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    public LoginResponse login(Bundle parameters) throws IOException, JSONException {
        final Map<String, String> parameterMap = new HashMap<>();
        for (String key : parameters.keySet()) {
            parameterMap.put(key, parameters.getString(key));
        }
        Response<LoginResponse> response = loginService.login(parameterMap).execute();
        return response.body();
    }






    @NonNull
    public SignupResponse signup(Bundle parameters) throws IOException {
        final Map<String, String> parameterMap = new HashMap<>();
        for (String key : parameters.keySet()) {
            parameterMap.put(key, parameters.getString(key));
        }
        Response<SignupResponse> response = loginService.register(parameterMap).execute();
        return response.body();
    }



}
