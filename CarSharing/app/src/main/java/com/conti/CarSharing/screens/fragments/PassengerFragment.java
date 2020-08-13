package com.conti.CarSharing.screens.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.conti.CarSharing.NotificationCreator;
import com.conti.CarSharing.R;
import com.conti.CarSharing.communication.Mqtt;
import com.conti.CarSharing.screens.activities.MainActivity;
import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.MapView;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.MarkerOptions;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class PassengerFragment extends Fragment {

    private GoogleMap gMap;
    private MapView mapChosePickPosition;
    private Mqtt mqtt;
    private Mqtt mqtt2;

    public PassengerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_passenger, parent, false);

        mapChosePickPosition = fragmentView.findViewById(R.id.passengerMap);
        mapChosePickPosition.onCreate(savedInstanceState);
        mapChosePickPosition.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (gMap == null) {
                    gMap = googleMap;
                    gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            gMap.clear();
                            gMap.addMarker(new MarkerOptions().position(latLng).title("Your destination!"));
                            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            gMap.animateCamera(CameraUpdateFactory.zoomTo(7.0f));
                            TextView title = new TextView((MainActivity) getContext());
                            title.setText("Add additional info for driver");
                            title.setGravity(Gravity.CENTER);
                            title.setTextSize(15);
                            title.setTypeface(null, Typeface.BOLD);
                            // variable to hold edit text
                            final EditText editTextField = new EditText((MainActivity) getContext());

                            // variable to hold location coordinates
                            final String location = latLng.latitude + " " + latLng.longitude + " ";

                            //popUp - Message was sent with success

                            final AlertDialog popUpMessageSendInfo = new AlertDialog.Builder((MainActivity) getContext())
                                    .setTitle("Message sent")
                                    .setMessage("Your message was sent with success. Please wait for the driver response.")
                                    .setPositiveButton("Ok", null)
                                    .create();

                            //popUp - Trip additional info

                            AlertDialog popUpTripAdditionalInfo = new AlertDialog.Builder((MainActivity) getContext())
                                    .setCustomTitle(title)
                                    //.setView(new EditText((MainActivity) getContext()))
                                    .setView(editTextField)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String messageToSend = editTextField.getText().toString();
                                            String message = messageToSend + location;
                                            System.out.println(" location " + message);

                                            String topic = "/aaa";
                                            System.out.println("Vom trimite mesajul: " + message + " pe topicul: " + topic);
                                            mqtt.publishAMessage(message, topic, false);
                                            popUpMessageSendInfo.show();

                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .create();
                            popUpTripAdditionalInfo.show();
                        }
                    });
                }

                gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(47.152842, 27.588228)));
                gMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            }
        });


        MqttCallback mqttCallback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Am pierdut conexiunea..");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Am primit mesajul \"" + message.toString() + "\"" + " topic: " + topic);
                NotificationCreator c = new NotificationCreator(getContext());
                c.sendNotification("test", 100);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Am trimis mesajul cu succes");
            }
        };

        mqtt = new Mqtt((MainActivity) getContext(), "tcp://10.0.2.2:1885", MqttClient.generateClientId(), mqttCallback);
        mqtt2 = new Mqtt((MainActivity) getContext(), "tcp://10.0.2.2:1885", MqttClient.generateClientId(), mqttCallback);

        mqtt.connectToBroker(new String[]{}, new int[]{});

        String[] topics = new String[]{"/test"};
        int[] qos = new int[]{1};
        mqtt2.connectToBroker(topics, qos);


        return fragmentView;
    }


}
