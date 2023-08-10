package com.example.snapchat.data;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface IMqttCallBack {
    void onActionSuccess(int action, IMqttToken asyncActionToken);
    void onActionFailure(int action, IMqttToken asyncActionToken, Throwable exception);
    void onActionFailure(int action, Exception e);
    void connectionLost(Throwable cause);
    void messageArrived(String topic, MqttMessage message);
    void deliveryComplete(IMqttDeliveryToken token);
}
