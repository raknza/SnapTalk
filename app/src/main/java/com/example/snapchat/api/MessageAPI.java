package com.example.snapchat.api;

import androidx.annotation.NonNull;
import com.example.snapchat.data.model.Message;
import com.example.snapchat.data.response.EmptyResponse;
import com.example.snapchat.service.MessageService;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Response;

@Singleton
public class MessageAPI {

    @NonNull
    private final MessageService messageService;

    @Inject
    public MessageAPI(@NonNull MessageService messageService) {
        this.messageService = messageService;
    }

    @NonNull
    public List<Message> getUnreceivedMessage() throws IOException {
        Response<List<Message>> response = messageService.getUnreceivedMessage().execute();
        return response.body();
    }

    @NonNull
    public EmptyResponse receivedMessage() throws IOException {
        Response<EmptyResponse> response = messageService.receivedMessage().execute();
        return response.body();
    }
}
