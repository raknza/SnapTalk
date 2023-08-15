package com.example.snapchat.service;


import androidx.annotation.NonNull;

import com.example.snapchat.data.model.Contact;
import com.example.snapchat.data.response.EmptyResponse;
import com.example.snapchat.http.ApiConstants;
import com.example.snapchat.provider.RetrofitProvider;
import java.util.Map;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ContactService {

    /**
     * A Provider implementation for ContactService.
     */
    @Module
    @InstallIn(SingletonComponent.class)
    class Provider {
        @Singleton
        @Provides
        public ContactService get(@NonNull RetrofitProvider retrofitProvider) {
            return retrofitProvider.get().create(ContactService.class);
        }
    }


    /**
     * Reset password for account associated with an email address.
     */
    @NonNull
    @FormUrlEncoded
    @POST(ApiConstants.URL_CREATE_CONTACT)
    Call<EmptyResponse> createContact(@FieldMap Map<String, String> parameters);
    /**
     * Reset password for account associated with an email address.
     */
    @NonNull
    @FormUrlEncoded
    @POST(ApiConstants.URL_DELETE_CONTACT)
    Call<EmptyResponse> deleteContact(@FieldMap Map<String, String> parameters);

    @NonNull
    @GET(ApiConstants.URL_MY_CONTACTS)
    Call<Contact[]> getContactList();

    @NonNull
    @GET(ApiConstants.URL_SEARCH_USER)
    Call<Contact> searchUser(@Path("username") String username);


}