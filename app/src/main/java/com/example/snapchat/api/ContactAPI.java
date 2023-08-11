package com.example.snapchat.api;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.data.response.EmptyResponse;
import com.example.snapchat.service.ContactService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Response;

@Singleton
public class ContactAPI {

    @NonNull
    private final ContactService contactService;

    @Inject
    public ContactAPI(@NonNull ContactService contactService) {
        this.contactService = contactService;
    }


    @NonNull
    public Contact[] getContactList() throws IOException {
        Response<Contact[]> response = contactService.getContactList().execute();
        return response.body();
    }

    @NonNull
    public Contact searchUser(String username) throws IOException {
        Response<Contact> response = contactService.searchUser(username).execute();
        return response.body();
    }

    @NonNull
    public EmptyResponse addContact(Bundle parameters) throws IOException {
        final Map<String, String> parameterMap = new HashMap<>();
        for (String key : parameters.keySet()) {
            parameterMap.put(key, parameters.getString(key));
        }
        Response<EmptyResponse> response = contactService.createContact(parameterMap).execute();
        return response.body();
    }

    @NonNull
    public EmptyResponse deleteContact(Bundle parameters) throws IOException {
        final Map<String, String> parameterMap = new HashMap<>();
        for (String key : parameters.keySet()) {
            parameterMap.put(key, parameters.getString(key));
        }
        Response<EmptyResponse> response = contactService.deleteContact(parameterMap).execute();
        return response.body();
    }
}
