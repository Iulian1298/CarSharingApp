package com.conti.CarSharing.screens.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.conti.CarSharing.LocationService;
import com.conti.CarSharing.R;
import com.conti.CarSharing.persistence.SharedPreferencesManager;
import com.conti.CarSharing.screens.activities.MainActivity;

import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class DriverFragment extends Fragment {
    private static String TAG = "DriverFragment";
    Intent serviceIntent;

    public DriverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_driver, container, false);

        SharedPreferencesManager preferencesManager = SharedPreferencesManager.getInstance(getContext());
        preferencesManager.setUserId(UUID.randomUUID().toString());

        Button b = fragmentView.findViewById(R.id.stopService);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceIntent = new Intent(getContext(), LocationService.class);
                ((MainActivity) getContext()).stopService(serviceIntent);
                Log.i(TAG, "Service stopped");
            }
        });
        startLocationService();

/*
        Mqtt mqtt;
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
                    //sendCurrentLocationToServer();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i(TAG, "Delivery completed");
            }
        };
        mqtt = new Mqtt(this.getContext(), "tcp://10.0.2.2:1884", clientId, mqttCallback);
        mqtt.connectToBroker(new String[]{"/other"}, new int[]{1});
*/
        return fragmentView;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            serviceIntent = new Intent(getContext(), LocationService.class);
            getContext().startService(serviceIntent);
        } else {
            Log.i(TAG, "Service already running!");
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
