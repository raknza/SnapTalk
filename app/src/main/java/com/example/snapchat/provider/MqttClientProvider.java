package com.example.snapchat.provider;

import androidx.annotation.NonNull;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import com.example.snapchat.data.IMqttCallBack;

public class MqttClientProvider implements MqttCallback {

    private IMqttAsyncClient client;
    private MqttConnectOptions mqttConnectOptions;
    private static MqttClientProvider instance;
    private IMqttCallBack iMqttCallBack;
    public static int ACTION_CONNECT = 301;
    public static int ACTION_SUBSCRIBE = 401;
    public static int ACTION_PUBLISH = 501;
    public static MqttClientProvider getInstance() {
        if (instance == null) {
            synchronized (MqttClientProvider.class) {
                if (instance == null) {
                    instance = new MqttClientProvider();
                }
            }
        }
        return instance;
    }
    private MqttClientProvider() {}

    public void init() {
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setKeepAliveInterval(300);
        mqttConnectOptions.setUserName("a".trim());
        mqttConnectOptions.setPassword("a".trim().toCharArray());
        mqttConnectOptions.setConnectionTimeout(30);
    }

    public void setIMqttCallBack(IMqttCallBack callBack) {
        this.iMqttCallBack = callBack;
    }

    public void removeIMqttCallBack() {
        this.iMqttCallBack = null;
    }
    public void connect(String username,String password) throws MqttException {
        mqttConnectOptions.setUserName(username.trim());
        mqttConnectOptions.setPassword(password.trim().toCharArray());
        MemoryPersistence persistence = new MemoryPersistence();
        if (client == null) {
            client = new MqttAsyncClient(getBaseUrl(),username,persistence);
        }
        try {
            client.setCallback(this);
            client.connect(mqttConnectOptions);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void sendData(byte[] data, String topic) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(data);
        try {
            client.publish(topic, mqttMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        if (client == null || !client.isConnected()) {
            return;
        }
        try {
            client.unsubscribe(topic);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            client.subscribe(topic,0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    public void release() throws MqttException {
        if (client !=null && client.isConnected()) {
            client.disconnect();
        }
        client = null;
        iMqttCallBack = null;
    }

    @Override
    public void connectionLost(Throwable cause) {
        if (iMqttCallBack != null) {
            iMqttCallBack.connectionLost(cause);
        }

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        if (iMqttCallBack != null) {
            iMqttCallBack.messageArrived(topic, message);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        if (iMqttCallBack != null) {
            iMqttCallBack.deliveryComplete(token);
        }

    }
    @NonNull
    private String getBaseUrl() {
        return "tcp://192.168.1.159:1883";
    }

}