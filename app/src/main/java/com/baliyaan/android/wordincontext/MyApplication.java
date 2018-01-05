package com.baliyaan.android.wordincontext;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Pulkit Singh on 1/5/2018.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
