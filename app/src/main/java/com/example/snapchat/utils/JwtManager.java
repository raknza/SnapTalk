package com.example.snapchat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.lang.ref.WeakReference;

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
}
