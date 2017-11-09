package com.baliyaan.android.wordincontext;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baliyaan.android.library.ads.Interstitial;

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

        // Display ads
        Interstitial interstitialAd = new Interstitial(this, getString(R.string.adId));
        interstitialAd.AllowOnlyAfter((long) 3.6e+5);// 6 minutes
        interstitialAd.AllowInDebug(false);
        interstitialAd.showEvery(1000000, true); // every 16.66 minutes

        // Search by intent (if any)
        Intent intent = getIntent();
        onIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        _navigator.onSaveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        _navigator.onRestoreState(savedInstanceState);
    }
}
