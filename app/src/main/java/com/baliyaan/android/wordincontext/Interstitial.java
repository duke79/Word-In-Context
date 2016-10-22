package com.baliyaan.android.wordincontext;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Pulkit on 01-Feb-16.
 */
public class Interstitial {
    private InterstitialAd mInterstitialAd;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private long mDelayMillis;
    Context mContext;

    /**
     * @param context
     * @param adUnitID
     */
    public Interstitial(Context context, String adUnitID) {
        mContext = context;
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(adUnitID);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
    }

    public void show() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {            
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    mInterstitialAd.show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // no interstitial available
                    mInterstitialAd = null;
                    Log.d("MyLog", "Ad failed to load");
                }
            });
        }
    }

    public void showEvery(long delayMillis, final boolean showNow) {
        mDelayMillis = delayMillis;
        mRunnable = new Runnable() {
            @Override
            public void run() {
                ((Activity)mContext).runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      if(showNow == true) {
                          show();
                      }
                      mHandler.postDelayed(mRunnable, mDelayMillis);
                  }
                });
            }
        };
        mHandler.post(mRunnable);
    }

    public void stopShowing() {
        mHandler.removeCallbacks(mRunnable);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }
}
