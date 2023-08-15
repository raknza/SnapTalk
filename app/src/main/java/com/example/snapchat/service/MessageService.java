package com.example.snapchat.service;

import androidx.annotation.NonNull;

import com.example.snapchat.data.model.Contact;
import com.example.snapchat.data.model.Message;
import com.example.snapchat.data.response.EmptyResponse;
import com.example.snapchat.http.ApiConstants;
import com.example.snapchat.provider.RetrofitProvider;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MessageService {

    /**
     * A Provider implementation for ContactService.
     */
    @Module
    @InstallIn(SingletonComponent.class)
    class Provider {
        @Singleton
        @Provides
        public MessageService get(@NonNull RetrofitProvider retrofitProvider) {
            return retrofitProvider.get().create(MessageService.class);
        }
    }

    @NonNull
    @GET(ApiConstants.URL_GET_UNRECEIVED_MESSAGE)
    Call<List<Message>> getUnreceivedMessage();

    @NonNull
    @GET(ApiConstants.URL_RECEIVED_MESSAGE)
    Call<EmptyResponse> receivedMessage();

}
