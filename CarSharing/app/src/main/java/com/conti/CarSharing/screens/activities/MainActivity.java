package com.conti.CarSharing.screens.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.conti.CarSharing.R;
import com.conti.CarSharing.screens.fragments.DriverFragment;
import com.conti.CarSharing.screens.fragments.PassengerFragment;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainActivity";

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //private SwitchCompat darkModeSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        getSupportActionBar().setTitle("Driver");
        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer, new DriverFragment(), "Driver").commit();

        //setDarkModeSwitchListener();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.i(TAG, "onNavigationItemSelected: select");
                switch (menuItem.getItemId()) {
                    case R.id.nav_menu1:
                        getSupportActionBar().setTitle("Driver");
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer, new DriverFragment(), "Driver").commit();
                        Log.i(TAG, "onNavigationItemSelected: ");
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        break;
                    case R.id.nav_menu2:
                        getSupportActionBar().setTitle("Passenger");
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer, new PassengerFragment(), "Passenger").commit();
                        Log.i(TAG, "onNavigationItemSelected: ");
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        break;
                }
                return true;
            }
        });

        toggleDrawer();
    }

    private void toggleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
