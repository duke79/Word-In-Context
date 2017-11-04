package com.baliyaan.android.wordincontext;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.baliyaan.android.library.ads.Interstitial;
import com.baliyaan.android.wordincontext.Components.Examples.MVP.ExamplesMVPContract;
import com.baliyaan.android.wordincontext.Components.Examples.MVP.ExamplesViewPortMVPViewPortAdapter;
import com.baliyaan.android.wordincontext.Components.SearchBox.MVP.UISearchBoxMVPContract;
import com.baliyaan.android.wordincontext.Components.SearchBox.MVP.UISearchBoxViewPortMVPViewPortAdapter;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
public class MainActivity extends AppCompatActivity implements UISearchBoxMVPContract.Navigator, ExamplesMVPContract.Navigator {

    static Context _context = null;
    public String _query = "dictionary";
    public final static String _BuildConfig = BuildConfig.DEBUG ? "debug" : "release";
    private UISearchBoxMVPContract.MVPPort _searchPort = null;
    private ExamplesMVPContract.MVPPort _examplesPort = null;
//    public LruCache<Integer,String> _cache = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = this;

//        int memClass = ( (ActivityManager) getSystemService( Context.ACTIVITY_SERVICE ) ).getMemoryClass();
//        int cacheSize = 1024 * 1024 * memClass / 8;
//        _cache = new LruCache<Integer,String>( cacheSize );

        // Enable home button
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Prepare UI
        setContentView(R.layout.activity_main);
        _searchPort = new UISearchBoxViewPortMVPViewPortAdapter(this,this);
        _examplesPort = new ExamplesViewPortMVPViewPortAdapter(this,this);

        // Display ads in release configurations
        if (_BuildConfig != "debug") {
            showAds();
        }

        // Search by intent (if any)
        Intent intent = getIntent();
        ProcessIntent(intent);

        TestRxAndroid();
    }

    private void TestRxAndroid() {
        Observable<String> observable = new Observable<String>() {
            @Override
            protected void subscribeActual(Observer observer) {
                observer.onNext("someText");
            }
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.i("RxAndroid",s);
            }
        });
    }

    private void showAds() {
        boolean hasMinInstallTimePassed = false;
        PackageManager pm = _context.getPackageManager();
        ApplicationInfo appInfo = null;
        try {
            appInfo = pm.getApplicationInfo(PACKAGE_NAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(null != appInfo) {
            String appFile = appInfo.sourceDir;
            long installed = new File(appFile).lastModified(); //Epoch Time
            long now = System.currentTimeMillis();
            if(now-installed > 3.6e+5) // 6 minutes
                hasMinInstallTimePassed =true;
        }
        /*
         * Show Ad every 5 minutes
         */
        if(hasMinInstallTimePassed ==true) {
            Interstitial interstitialAd = new Interstitial(_context, getString(R.string.adId));
            interstitialAd.showEvery(1000000, true); // every 16.66 minutes
        }
    }

    private void ProcessIntent(Intent intent) {
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
        _searchPort.setQuery(intentQuery);
    }

    @Override
    protected void onStop() {
        super.onStop();
        _examplesPort.onSaveState();
        _searchPort.onSaveState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        _examplesPort.onResumeState();
        _searchPort.onResumeState();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ProcessIntent(intent);
    }

    @Override //UISearchBoxMVPContract.Navigator
    public void onSearchBoxSubmit(String query) {
        _query = query;
        _examplesPort.onQueryTextSubmit(query);
    }

    @Override //UISearchBoxMVPContract.Navigator
    public void onTryAgain() {
        _examplesPort.onQueryTextSubmit(_query);
    }
}
