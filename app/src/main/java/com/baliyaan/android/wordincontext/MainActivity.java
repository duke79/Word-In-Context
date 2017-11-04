package com.baliyaan.android.wordincontext;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baliyaan.android.library.ads.Interstitial;

import java.io.File;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class MainActivity extends AppCompatActivity {

    Navigator _navigator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable home button
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Prepare UI
        setContentView(R.layout.activity_main);
        _navigator = new Navigator(this);

        // Display ads in release configurations
        String _BuildConfig = BuildConfig.DEBUG ? "debug" : "release";
        if (_BuildConfig != "debug") {
            showAds();
        }

        // Search by intent (if any)
        Intent intent = getIntent();
        onIntent(intent);
    }

    private void showAds() {
        boolean hasMinInstallTimePassed = false;
        PackageManager pm = getPackageManager();
        ApplicationInfo appInfo = null;
        try {
            appInfo = pm.getApplicationInfo(PACKAGE_NAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (null != appInfo) {
            String appFile = appInfo.sourceDir;
            long installed = new File(appFile).lastModified(); //Epoch Time
            long now = System.currentTimeMillis();
            if (now - installed > 3.6e+5) // 6 minutes
                hasMinInstallTimePassed = true;
        }
        /*
         * Show Ad every 5 minutes
         */
        if (hasMinInstallTimePassed == true) {
            Interstitial interstitialAd = new Interstitial(this, getString(R.string.adId));
            interstitialAd.showEvery(1000000, true); // every 16.66 minutes
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        _navigator.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        _navigator.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onIntent(intent);
    }

    private void onIntent(Intent intent) {
        ClipData clipData = intent.getClipData();
        if (clipData == null)
            return;
        ClipData.Item item = clipData.getItemAt(0);
        if (item == null)
            return;
        String intentQuery = item.getText().toString();
        if (intentQuery == null)
            return;
        if (!(intentQuery.length() > 0))
            return;
        _navigator.onSearchIntent(intentQuery);
    }
}
