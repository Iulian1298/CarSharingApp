package com.conti.CarSharing.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class SharedPreferencesManager {

    private static final String TAG = "SharedPreferencesManager";
    private static final String mUrl = "http://10.0.2.2:5000";
    //private static final String mUrl = "https://agile-harbor-57300.herokuapp.com";


    private static final String SHARED_PREF_NAME = "CarSharing";
    private static final String KEY_IS_LOGGED = "isLogged";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_MAIL = "userMail";
    private static final String KEY_USER_PHONE = "userPhone";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_PERMISSION_LOCATION = "location";
    private static final String KEY_ONLY_MY_SERVICES = "onlyMyServices";


    private static SharedPreferencesManager mInstance;
    private static Context mContext;

    private SharedPreferencesManager(Context ctx) {
        mContext = ctx;
    }


    public static synchronized SharedPreferencesManager getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new SharedPreferencesManager(ctx);
        }
        return mInstance;
    }

    public synchronized void setUserId(String id) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, id);
        editor.apply();
    }

    public synchronized String getUserId() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public void logout() {
        Log.i(TAG, "SharedPreferenceManager::logout()");
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
