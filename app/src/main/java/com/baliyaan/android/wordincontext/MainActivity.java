package com.baliyaan.android.wordincontext;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baliyaan.android.library.ads.Interstitial;
import com.baliyaan.android.wordincontext.Model.Dictionary;
import com.baliyaan.android.wordincontext.Model.WordExample;
import com.baliyaan.android.wordincontext.UI.ExamplesAdapter;
import com.baliyaan.android.wordincontext.UI.SearchBox.UISearchBoxContract;
import com.baliyaan.android.wordincontext.UI.SearchBox.UISearchBoxView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static com.baliyaan.android.wordincontext.Data.Scraper.GetExamples;
public class MainActivity extends AppCompatActivity implements UISearchBoxContract.Navigator {

    public static final String TAG = "ContextDictionary";
    ArrayList<WordExample> _examples = new ArrayList<WordExample>();
    static Context _context = null;
    ViewPager _viewPager = null;
    PagerAdapter _pagerAdapter = null;
    public String _query = "dictionary";
    Dictionary _dictionary = null;
    public final static String _BuildConfig = BuildConfig.DEBUG ? "debug" : "release";
    private UISearchBoxContract.Port _searchView = null;
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
        _searchView  = new UISearchBoxView(this,this);
        prepareContentView();

        // Display ads in release configurations
        if (_BuildConfig != "debug") {
            showAds();
        }

        // Search by intent (if any)
        Intent intent = getIntent();
        ProcessIntent(intent);

        //Load dictionary
        _dictionary = Dictionary.GetInstance(_context);

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
        _searchView.setQuery(intentQuery);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ProcessIntent(intent);
    }

    private void prepareContentView() {
        _viewPager = (ViewPager) findViewById(R.id.view_pager_examples);
        _pagerAdapter = new ExamplesAdapter(this, _examples);
        if (null != _viewPager) {
            _viewPager.setAdapter(_pagerAdapter);
        }
    }

    @Override
    public void onSearchBoxSubmit(String query) {
        _query = query;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<WordExample> newList = GetExamples(_query);
                    if (newList.size() > 0) {
                        Handler handler = new Handler(_context.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (_pagerAdapter != null) {
                                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                                    findViewById(R.id.view_pager_examples).setVisibility(View.VISIBLE);
                                    _examples.removeAll(_examples);
                                    _examples.addAll(newList);
                                    _pagerAdapter.notifyDataSetChanged();
                                    _viewPager.setCurrentItem(0);
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    ((MainActivity) _context).runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(_context, "No internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
