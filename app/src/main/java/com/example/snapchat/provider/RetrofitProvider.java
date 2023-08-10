package com.example.snapchat.provider;


import androidx.annotation.NonNull;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public interface RetrofitProvider {

    @NonNull
    Retrofit get();

    @Singleton
    class Impl implements RetrofitProvider {
        @Inject
        OkHttpClientProvider clientProvider;

        @Inject
        Gson gson;

        @Inject
        public Impl() {
        }

        private Retrofit retrofit ;

        @NonNull
        @Override
        public Retrofit get() {
            return get(clientProvider.get());
        }


        @NonNull
        private synchronized Retrofit get(@NonNull final OkHttpClient client) {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .client(client)
                        .baseUrl(getBaseUrl())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
            return retrofit;
        }

        @NonNull
        private String getBaseUrl() {
            return "https://192.168.1.159:5001/";
        }
    }
}