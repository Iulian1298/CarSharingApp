package com.conti.CarSharing;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.conti.CarSharing.communication.Mqtt;
import com.conti.CarSharing.persistence.SharedPreferencesManager;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;


public class LocationService extends Service {

    private static String TAG = "Service";
    private Mqtt mqtt;
    private AtomicBoolean working = new AtomicBoolean(true);
    private ReentrantLock lock = new ReentrantLock();
    private int availabilitySent = 0;
    private SharedPreferencesManager preferencesManager;

    public boolean isOnline() {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            Log.d(TAG, "Internet available!");
            return true;
        } catch (IOException e) {
            Log.d(TAG, "Internet not available!");
            return false;
        }
    }

    private void sendCurrentLocationToServer() {

        double latitude = 47.12412;
        double longitude = 27.12412;
        String topic = "/position";
        String message = latitude + ";" + longitude + " " + preferencesManager.getUserId();
        int retCode = mqtt.publishAMessage(message, topic, false);
        if (retCode == 1) {
            Log.i(TAG, "sendCurrentLocationToServer: position published on topic: " + topic);
        } else {
            if (retCode == 0) {
                Log.e(TAG, "sendCurrentLocationToServer: position not published. See above errors!");
            } else {
                Log.e(TAG, "sendCurrentLocationToServer: position not published. Not connected to broker yet!");
            }
        }
    }

    private int sendAvailabilityToServer(boolean value) {
        String topic = "/" + preferencesManager.getUserId() + "/availability";
        int[] qos = new int[]{1, 2};
        int retCode = mqtt.publishAMessage(String.valueOf(value), topic, false);
        if (retCode == 1) {
            Log.i(TAG, "sendAvailabilityToServer: availability published on topic: " + topic);
        } else {
            if (retCode == 0) {
                Log.e(TAG, "sendAvailabilityToServer: availability not published. See above errors!");
            } else {
                Log.e(TAG, "sendAvailabilityToServer: availability not published. Not connected to broker yet!");
            }
        }
        return retCode;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (working.get()) {
                try {
                    Thread.sleep(1000);
                    //lock.lock();
                    /*if (preferencesManager.getUserId() != -1) {
                        if (isOnline() && availabilitySent != 1) {
                            //sendCurrentLocationToServer();
                            availabilitySent = sendAvailabilityToServer(true);
                            if (availabilitySent == 1) {
                                //mqtt.subscribeToTopics(new String[]{"/" + preferencesManager.getUserId() + "/getPosition"}, new int[]{1});
                            }
                        }
                    }*/
                    //lock.unlock();
                    if (isOnline()) {
                        //mqtt.connectToBroker(new String[]{"/" + preferencesManager.getUserId() + "/getPosition"}, new int[]{1});
                        mqtt.subscribeToTopics(new String[]{"/" + preferencesManager.getUserId() + "/getPosition"}, new int[]{1});
                        working.get();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                Log.d(TAG, "Internet is down!!");
            }
            Log.d(TAG, "Reconnected to internet. Trying to reconnect!!");

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        preferencesManager = SharedPreferencesManager.getInstance(getApplicationContext());

        /*lock.lock();
        userId = -1;
        if (intent != null) {
            userId = intent.getIntExtra("LoggedUser.id", -1);
        }*/
        if (preferencesManager.getUserId() == null) {
            this.stopSelf();
        } else {
            //if (isOnline()) {
            createMqttConnection();
            //}
        }
        //lock.unlock();

        //while (availabilitySent != 1) {
        //availabilitySent = sendAvailabilityToServer(true);
        //}
        availabilitySent = 0;
        //new Thread(runnable).start();
        return START_STICKY;
    }

    private void createMqttConnection() {
        String clientId = MqttClient.generateClientId();
        MqttCallback mqttCallback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.e(TAG, "Connection lost...");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i(TAG, "messageArrived: " + topic + " message: " + message);
                if (true)  //check if message is the expected one
                {
                    sendCurrentLocationToServer();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i(TAG, "Delivery completed");
            }
        };

        mqtt = new Mqtt(this.getApplicationContext(), "tcp://10.0.2.2:1885", clientId, mqttCallback);
        mqtt.connectToBroker(new String[]{"/getPosition"}, new int[]{1});
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        //working.set(false);
        //sendAvailabilityToServer(false);
        mqtt.unSubscribeFromTopics(new String[]{"/getPosition"});
        mqtt.disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
