package com.pradipatle.cityguide.tumsar.Utils;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

public class Application extends android.app.Application {
    private static final String TAG = "ApplicationLogs";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
}