package com.baliyaan.android.wordincontext;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Pulkit Singh on 1/5/2018.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();

        /*LeakCanary*/
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        /*Stetho*/
        Stetho.initializeWithDefaults(this);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
