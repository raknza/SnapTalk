package com.example.snapchat.provider;

import androidx.annotation.NonNull;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import com.example.snapchat.data.IMqttCallBack;

public class MqttClientProvider {

    private MqttClient client;
    private MqttConnectOptions mqttConnectOptions;
    private static MqttClientProvider instance;
    private IMqttCallBack iMqttCallBack;
    public static int ACTION_CONNECT = 301;
    public static int ACTION_SUBSCRIBE = 401;
    public static int ACTION_PUBLISH = 501;
    private MqttCallback mCallBack;
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
        mqttConnectOptions.setConnectionTimeout(100);
        mqttConnectOptions.setAutomaticReconnect(true);
        mCallBack = new MqttCallback() {
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
        };

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
            client = new MqttClient(getBaseUrl(),username,persistence);
        }
        try {
            client.connect(mqttConnectOptions);
            client.setCallback(mCallBack);

        } catch (MqttException e) {
            if (iMqttCallBack != null) {
                iMqttCallBack.onActionFailure(ACTION_CONNECT, e);
            }
            e.printStackTrace();
        }
    }

    public void sendData(byte[] data, String topic) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(data);
        try {
            client.publish(topic, mqttMessage);
        } catch (Exception e) {
            if (iMqttCallBack != null) {
                iMqttCallBack.onActionFailure(ACTION_PUBLISH, e);
            }
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
            client.subscribe(topic);

        } catch (MqttException e) {
            if (iMqttCallBack != null) {
                iMqttCallBack.onActionFailure(ACTION_SUBSCRIBE, e);
            }
            e.printStackTrace();
        }
    }
    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    public MqttClient getClient() {
        return client;
    }

    public void release() throws MqttException {
        if (client !=null && client.isConnected()) {
            client.disconnect();
        }
        client = null;
        iMqttCallBack = null;
    }

    @NonNull
    private String getBaseUrl() {
        return "tcp://192.168.1.159:1883";
    }

}