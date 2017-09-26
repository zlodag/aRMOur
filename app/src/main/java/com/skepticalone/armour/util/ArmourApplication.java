package com.skepticalone.armour.util;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public final class ArmourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}