package com.example.snapchat.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.myapplication.R;
import com.example.snapchat.MainActivity;
import com.example.snapchat.data.IMqttCallBack;
import com.example.snapchat.data.model.Message;
import com.example.snapchat.provider.DefaultModule;
import com.example.snapchat.provider.MqttClientProvider;
import com.example.snapchat.utils.DataManager;
import com.example.snapchat.data.model.Contact;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import dagger.hilt.android.EntryPointAccessors;


public class MessageReceiveService extends Service implements IMqttCallBack {

    static int notificationId = 1;
    private static final int NOTIFICATION_ID = 123;
    MqttClientProvider mqttClientProvider;
    Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        gson = EntryPointAccessors.fromApplication(getApplicationContext(), DefaultModule.ProviderEntryPoint.class).getGSon();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "SnapTalk")
                .setContentTitle("SnapTalk")
                .setContentText("Service is running")
                .setSmallIcon(R.drawable.snap_2)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        Notification notification = notificationBuilder.build();

        startForeground(NOTIFICATION_ID, notification);

        DataManager.getInstance().getUserLiveData().observeForever(user-> {
            connect();
        });
        return START_REDELIVER_INTENT;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "SnapTalk";
            String channelName = "Foreground Service Channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription("Channel description");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void connect(){
        // mqtt connect
        mqttClientProvider = MqttClientProvider.getInstance();
        if(mqttClientProvider.isConnected() == false) {
            mqttClientProvider.init();
            mqttClientProvider.setIMqttCallBack(this);
            try {
                mqttClientProvider.connect(DataManager.getInstance().getUserLiveData().getValue().username, DataManager.getInstance().getUserLiveData().getValue().password);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
            // subscribe user relations topic
            mqttClientProvider.subscribe(DataManager.getInstance().getUserLiveData().getValue().username);
            DataManager.getInstance().getContactListLiveData().observeForever( list -> {
                for(Contact contact:list){
                    mqttClientProvider.subscribe("message/" + contact.username + "/" + DataManager.getInstance().getUserLiveData().getValue().username);
                }
            });
        }
    }

    @Override
    public void onActionSuccess(int action, IMqttToken asyncActionToken) {

    }

    @Override
    public void onActionFailure(int action, IMqttToken asyncActionToken, Throwable exception) {

    }

    @Override
    public void onActionFailure(int action, Exception e) {

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String jsonMessage = new String(message.getPayload());
        try {
            Message receivedMessage = gson.fromJson(jsonMessage, Message.class);
            DataManager.getInstance().addMessageForSender(receivedMessage.senderId, receivedMessage);
            showNotification(topic,receivedMessage.content);
        } catch (JsonSyntaxException e) {
            Log.e("JSON Parse Error", "Error parsing JSON: " + jsonMessage, e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    private void showNotification(String topic, String message) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=topic.indexOf('/')+1;topic.charAt(i) != '/';i++){
            stringBuilder.append(topic.charAt(i));
        }
        String partner = stringBuilder.toString();
        stringBuilder.append(" ").append(message);;
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                .setSmallIcon(R.mipmap.snap)
                .setContentText(stringBuilder)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("openChatroom", partner);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        notificationManager.notify(notificationId, builder.build());
    }
}
