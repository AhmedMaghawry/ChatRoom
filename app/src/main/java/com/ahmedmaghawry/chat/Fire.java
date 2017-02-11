package com.ahmedmaghawry.chat;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Ahmed Maghawry on 2/8/2017.
 */
public class Fire extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
