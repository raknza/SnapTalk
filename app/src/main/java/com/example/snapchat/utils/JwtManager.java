package com.example.snapchat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.snapchat.provider.DefaultModule;

import java.lang.ref.WeakReference;

import dagger.hilt.android.EntryPointAccessors;

public class JwtManager {

    private static String PREFS_NAME = "token";


    public static void saveJwtToSharedPreferences(Context context, String jwt) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwt", jwt);
        editor.apply();
    }

    public static String getJwtFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("jwt", null);
    }

    public static void removeJwt(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("jwt");
        editor.apply();
    }

    public static void addJwtInterceptor(Context context){
        // add jwt for each request
        JwtInterceptor jwtInterceptor = (JwtInterceptor) EntryPointAccessors.fromApplication(context
                        , DefaultModule.ProviderEntryPoint.class).getOkHttpClientProvider()
                .get().interceptors().get(0);
        jwtInterceptor.setToken(getJwtFromSharedPreferences(context));
    }
}
