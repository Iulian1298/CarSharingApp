package com.conti.CarSharing.screens.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.conti.CarSharing.R;
import com.conti.CarSharing.communication.Mqtt;


/**
 * Activity that user could login
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText usernameText, editTextPassword;
    private Button loginButton, signupButton;
    private Mqtt mqtt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeLoginView();
    }

    private void initializeLoginView() {

        //Initializing the different components of the view
        usernameText = findViewById(R.id.editText_userName_LoginActivity);
        editTextPassword  = findViewById(R.id.editText_password_SignupActivity);
        //carNumber = findViewById(R.id.editText_carNumber_LoginActivity);

//        passengerRadio = findViewById(R.id.radioButton_passenger_LoginActivity);
//        driverRadio = findViewById(R.id.radioButton_driver_LoginActivity);
//
//        driverRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if( b == true )
//                    carNumber.setVisibility(View.VISIBLE);
//                else {
//                    carNumber.setVisibility(View.GONE);
//                }
//            }
//        });

        loginButton = findViewById(R.id.button_login_LoginActivity);
        assert loginButton != null;
        loginButton.setOnClickListener(this);

        signupButton = findViewById(R.id.button_signup_LoginActivity);
        assert signupButton != null;
        signupButton.setOnClickListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

//    public void onRadioButtonClicked(View view) {
//
//        boolean checked = ((RadioButton) view).isChecked();
//        // Check which radio button was clicked
//
//        switch(view.getId()) {
//            case R.id.radioButton_passenger_LoginActivity:
//                if (checked)
//                    passengerRadio.setTypeface(null, Typeface.BOLD);
//                    driverRadio.setTypeface(null, Typeface.NORMAL);
//
//                    // will login as passenger
//                    //roleSel = "P";
//                    break;
//
//            case R.id.radioButton_driver_LoginActivity:
//                if (checked)
//                    driverRadio.setTypeface(null, Typeface.BOLD);
//                    passengerRadio.setTypeface(null, Typeface.NORMAL);
//
//                    // will login as driver
//                    //roleSel = "D";
//                    break;
//        }
//    }

//    public void enableEdit(boolean state) {
//        carNumber.setEnabled(state);
//        carNumber.setFocusable(state);
//        if(state){ carNumber.setInputType(InputType.TYPE_CLASS_TEXT); }
//        else { carNumber.setInputType(InputType.TYPE_NULL); }
//    }

    @Override
    public void onClick(View view) {
        if (view == loginButton ) {
            login();
        } else if (view == signupButton){
            // intent to SignupActivity
            Intent intentSignup = new Intent(this, SignUpActivity.class);
            startActivity(intentSignup);
        }
    }

    public void login(){
        String username = usernameText.getText().toString();
        boolean validUsername = !(username.isEmpty() || username.trim().isEmpty());

        if ( !(validUsername) ){
            Toast.makeText(this, "Username entered is incorrect.", Toast.LENGTH_SHORT).show();
            openUsernameDialog();
        }

        if (username == null) {
            Toast.makeText(this, "User does not exist, please signup", Toast.LENGTH_SHORT).show();
        }
        //else {
//
           //Intent intentPassengerFragment = new Intent(this, MainActivity.class);
            //startActivity(intentPassengerFragment);}
//            if (passengerRadio.isChecked()){
//
//                intentPassengerFragment.putExtra("isDriverOrPassenger",true);
//                startActivity(intentPassengerFragment);
//            }
//            if (driverRadio.isChecked()){
//                //Intent intentPassengerFragment = new Intent(this, MainActivity.class);
//
//                intentPassengerFragment.putExtra("isDriverOrPassenger",false);
//                startActivity(intentPassengerFragment);
//            }
//            if (!passengerRadio.isChecked() && !driverRadio.isChecked()){
//                openSelRoleDialog();
////            }


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
    //}

    private void openUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Please specify your username.")
                .setPositiveButton("Ok", null);

        // Create & Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
