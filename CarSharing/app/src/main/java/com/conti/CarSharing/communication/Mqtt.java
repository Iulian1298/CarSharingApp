package com.conti.CarSharing.communication;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;


public class Mqtt {

    public final MqttAndroidClient client;
    private final MqttConnectOptions options = new MqttConnectOptions();
    private String TAG = "Mqtt";
    private boolean isConnectedToBroker = false;

    /**
     * constructor
     *
     * @param ctx
     * @param serverUri
     * @param clientId
     * @param mqttCallback
     */
    public Mqtt(Context ctx, String serverUri, String clientId, MqttCallback mqttCallback) {
        client = new MqttAndroidClient(ctx, serverUri, clientId);
        client.setCallback(mqttCallback);
        options.setConnectionTimeout(0);
        options.setCleanSession(false);
    }

    /**
     * method which connect to MQTT server and subscribe to topics from topic string array if not empty
     *
     * @param topic
     * @param qos
     */
    public void connectToBroker(final String[] topic, final int[] qos) {
        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //Log.d(TAG, "Success");
                    Log.d(TAG, "M-am conectat cu succes la broker");
                    isConnectedToBroker = true;
                    if (topic.length != 0) {
                        subscribeToTopics(topic, qos);
                    }
                    //unSubscribeFromTopics("test/topic");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //Log.d(TAG, "Failed to connect to broker " + exception.toString());
                    Log.e(TAG, "Nu reusesc sa ma conectez la broker din cauza: " + exception.toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * method which subscribe to topics from topic array
     *
     * @param topic
     * @param qos
     */
    public void subscribeToTopics(final String[] topic, int[] qos) {
        try {
            //if (client.isConnected()) {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                    //Log.d(TAG, "Subscribed" + topic);
                    Log.d(TAG, "Subscribed la: " + topic[0].toString());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                    // Log.d(TAG, "Nu am reusit");
                    Log.e(TAG, "Fail to subscribe due to: " + exception.toString());
                }
            });
            // }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * method which unsubscribe to topics from topic array
     *
     * @param topics
     */
    public void unSubscribeFromTopics(String topics[]) {
        try {
            IMqttToken unsubToken = client.unsubscribe(topics);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The subscription could successfully be removed from the client
                    Log.d(TAG, "Success to unSubscribe");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    Log.e(TAG, "Fail to unSubscribe due to: " + exception.toString());
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * method which publish a message to a topic with retained flag
     *
     * @param message
     * @param topic
     * @param retained
     */
    public int publishAMessage(String message, String topic, boolean retained) {
        MqttMessage msg = new MqttMessage(message.getBytes());
        msg.setQos(2);
        msg.setRetained(retained);
        if (isConnectedToBroker) {
            try {
                client.publish(topic, msg);
                return 1;
            } catch (MqttException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return -1;
        }
    }

    /**
     * disconnect from MQTT server
     */
    public void disconnect() {
        try {
            Log.d(TAG, "Mqtt is disconnecting from server..");
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
