package com.conti.CarSharing.screens.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import com.conti.CarSharing.R;
import com.conti.CarSharing.communication.Mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Activity that user could signup
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextPhone, carNumber, nrSeats;
    private Button signupButton;
    private Mqtt mqtt;

    private RadioButton passengerRadio, driverRadio;
    private String roleSel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeSignUpView();
        init();
    }

    private void initializeSignUpView() {

        editTextUsername = findViewById(R.id.editText_userName_SignupActivity);
        editTextEmail = findViewById(R.id.editText_email_SignupActivity);
        editTextPassword  = findViewById(R.id.editText_password_SignupActivity);
        editTextPhone = findViewById(R.id.editText_mobile_SignupActivity);
        carNumber = findViewById(R.id.editText_carNumber_LoginActivity);
        nrSeats =  findViewById(R.id.editText_nrSeats_LoginActivity);

        passengerRadio = findViewById(R.id.radioButton_passenger_LoginActivity);
        driverRadio = findViewById(R.id.radioButton_driver_LoginActivity);

        driverRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    carNumber.setVisibility(View.VISIBLE);
                    nrSeats.setVisibility(View.VISIBLE);
                } else {
                    carNumber.setVisibility(View.GONE);
                    nrSeats.setVisibility(View.GONE);
                }
            }
        });

        signupButton = findViewById(R.id.button_signup_SignupActivity);
        assert signupButton != null;
        signupButton.setOnClickListener(this);
    }

    private void init() {

        String clientId = MqttClient.generateClientId();
        MqttCallback mqttCallback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Am pierdut conexiunea..");
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Am primit mesajul \"" + message.toString() + "\"" + " topic: " + topic);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Am trimis mesajul cu succes");
            }
        };
        mqtt = new Mqtt(this.getApplicationContext(), "tcp://10.0.2.2:1883", clientId, mqttCallback);
        mqtt.connectToBroker(new String[]{}, new int[]{});
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked

        switch(view.getId()) {
            case R.id.radioButton_passenger_LoginActivity:
                if (checked)
                    passengerRadio.setTypeface(null, Typeface.BOLD);
                driverRadio.setTypeface(null, Typeface.NORMAL);

                // will login as passenger
                //roleSel = "P";
                break;

            case R.id.radioButton_driver_LoginActivity:
                if (checked)
                    driverRadio.setTypeface(null, Typeface.BOLD);
                passengerRadio.setTypeface(null, Typeface.NORMAL);

                // will login as driver
                //roleSel = "D";
                break;
        }
    }

    private void sendToServerForVerify() {
//        String[] topics = new String[]{"/serverResponse/login/email", "/serverResponse/login/password"};
//        int[] qos = new int[]{1, 2};
//        mqtt.subscribeToTopics(topics, qos);

        String[] topic1 = new String[]{"/serverResponse/" + editTextEmail.getText().toString().trim()};
        mqtt.subscribeToTopics(topic1,new int[]{1});

        //System.out.println("Este conectat? " + mqtt.client.isConnected());
        String messageToSend = editTextUsername.getText().toString().trim() + ";" +
                               editTextEmail.getText().toString().trim() + ";" +
                               editTextPassword.getText().toString().trim() +  ";" +
                               editTextPhone.getText().toString().trim();

        String topic = "register";
        System.out.println("Vom trimite mesajul: " + messageToSend + " pe topicul: " + topic);
        //mqtt.publishAMessage(editTextEmail.getText().toString().trim() + ";" + editTextPassword.getText().toString().trim(), topic, false);
        mqtt.publishAMessage(messageToSend, topic, false);
        System.out.println("Data sent to server for processing... ");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (view == signupButton) {
            signUp();
//            Intent intentSignup = new Intent(this, SignUpActivity.class);
//            startActivity(intentSignup);
            sendToServerForVerify();
        }
    }

    public void signUp() {

//        String username = editTextUsername.getText().toString();
//        String email = editTextEmail.getText().toString();
//        String password = editTextPassword.getText().toString();
//        String phone = editTextPhone.getText().toString();
//
//        boolean validUsername = !(username.isEmpty() || username.trim().isEmpty());
//        boolean validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();
//        boolean validPhone= Patterns.PHONE.matcher(phone).matches();
//
//        if (!(validUsername && validEmail && validPhone)) {
//            Toast.makeText(this, "Username/Email/Phone is not valid.", Toast.LENGTH_SHORT).show();
//
//
//        }
        String username = editTextUsername.getText().toString();
        boolean validUsername = !(username.isEmpty() || username.trim().isEmpty());

        if ( !(validUsername) ){
            Toast.makeText(this, "Username entered is incorrect.", Toast.LENGTH_SHORT).show();
        }

        if (username == null) {
            Toast.makeText(this, "User does not exist, please signup", Toast.LENGTH_SHORT).show();
        } else {
            Intent intentPassengerFragment = new Intent(this, MainActivity.class);
            if (passengerRadio.isChecked()){

                intentPassengerFragment.putExtra("isDriverOrPassenger",true);
                startActivity(intentPassengerFragment);
            }
            if (driverRadio.isChecked()){
                //Intent intentPassengerFragment = new Intent(this, MainActivity.class);

                intentPassengerFragment.putExtra("isDriverOrPassenger",false);
                startActivity(intentPassengerFragment);
            }
            if (!passengerRadio.isChecked() && !driverRadio.isChecked()){
                openSelRoleDialog();
            }


            //startActivity(intentPassengerFragment);
//            try {
//                if (roleSel.equals("P")) {
//                    // intent to PassengerFragment
//                    //MainActivity parentActivity = this;
//                    Toast.makeText(this, "login pressed", Toast.LENGTH_SHORT).show();
//                    Intent intentPassengerFragment = new Intent(this, PassengerFragment.class);
//
//                    //intentPassengerFragment.setFlag(Activity.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intentPassengerFragment);
//
//                } else if (roleSel.equals("D")) {
//                    // intent to DriverMainActivity
//                    //Intent intentDriverMain = new Intent(this, DriverMainActivity.class);
//                    // startActivity(intentDriverMain);
//                }
//
//            } catch (Exception e) {
//                openSelRoleDialog();
//                e.printStackTrace();
//            }
        }
    }

    private void openSelRoleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle("Please Specify Your Role (Passenger/Driver).")
                .setPositiveButton("Ok", null);

        // Create & Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}