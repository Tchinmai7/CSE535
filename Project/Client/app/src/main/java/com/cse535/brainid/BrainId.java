package com.cse535.brainid;

import android.app.Application;

import io.realm.Realm;

public class BrainId extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
    }
}
