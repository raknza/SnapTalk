package com.example.snapchat.utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {
    private String jwtToken;

    public JwtInterceptor() {
    }

    public void setToken(String token){ this.jwtToken = token; }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Log.e("intercept", "what??" + jwtToken);
        Request.Builder requestBuilder = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + jwtToken);

        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }
}