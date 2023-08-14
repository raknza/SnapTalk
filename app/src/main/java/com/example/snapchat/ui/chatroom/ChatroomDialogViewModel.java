package com.example.snapchat.ui.chatroom;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.snapchat.adapter.MessageAdapter;
import com.example.snapchat.data.model.ChatPartner;
import com.example.snapchat.data.model.Message;
import com.example.snapchat.provider.MqttClientProvider;
import com.example.snapchat.utils.DataManager;
import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ChatroomDialogViewModel extends ViewModel {

    MutableLiveData<MessageAdapter> messageAdapter;
    MutableLiveData<String> textMessage;
    List<Message> messageList;
    private MutableLiveData<ChatPartner> chatPartner;
    private MutableLiveData<Boolean> clearEditText ;
    private MqttClientProvider mqttClientProvider ;
    private String sendTopic;
    Gson gson;

    public LiveData<List<Message>> getMessagesWithSender() {
        return DataManager.getInstance().getMessagesWithPartner(chatPartner.getValue().contact.id);
    }

    public ChatroomDialogViewModel() {
        textMessage = new MutableLiveData<>();
        chatPartner = new MutableLiveData<>();
        clearEditText = new MutableLiveData<>();
        messageAdapter = new MutableLiveData<>();
        mqttClientProvider = MqttClientProvider.getInstance();
    }

    public void setChatPartner(ChatPartner partner){
        chatPartner.setValue(partner);
        sendTopic = DataManager.getInstance().getUserLiveData().getValue().username + "." + partner.contact.username;
        messageList = DataManager.getInstance().getMessagesWithPartner(partner.contact.id).getValue();
        messageAdapter.setValue(new MessageAdapter(messageList));
        messageAdapter.getValue().setPartner(partner);
    }

    public MutableLiveData<MessageAdapter> getMessageAdapter() { return  messageAdapter; }
    public MutableLiveData<String> getTextMessage() { return textMessage; }
    public LiveData<Boolean> getClearEditText() { return clearEditText; }
    public LiveData<ChatPartner> getChatPartner() { return chatPartner; }

    private void sendMessage(Message message){
        String jsonMessage = gson.toJson(message);
        mqttClientProvider.sendData(jsonMessage.getBytes(StandardCharsets.UTF_8),sendTopic);
        DataManager.getInstance().addMessageForSender(message);
        messageAdapter.getValue().updateData();
    }

    public void sendMessage(){
        Message message = new Message();
        message.content = textMessage.getValue();
        message.messageType = Message.MessageType.TEXT;
        message.senderId = DataManager.getInstance().getUserLiveData().getValue().id;
        message.recipientId = chatPartner.getValue().contact.id;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.TAIWAN);
        message.timestamp = dateFormat.format(new Date());
        textMessage.setValue("");
        sendMessage(message);
        clearEditText.setValue(true);
    }

    public void setGson(Gson gson){
        this.gson = gson;
    }

    public void receiveMessage() {
        LiveData<List<Message>> messagesLiveData = DataManager.getInstance().getMessagesWithPartner(chatPartner.getValue().contact.id);
        List<Message> messages = messagesLiveData.getValue();
        if (messages != null ) {
            if(messages.size() != 0)
                messageAdapter.getValue().updateData();
            else
                messageAdapter.getValue().updateData(messages);
        }
    }

}