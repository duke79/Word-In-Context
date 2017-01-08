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
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static com.baliyaan.android.wordincontext.Scraper.GetExamples;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "ContextDictionary";
    ArrayList<WordExample> _examples = new ArrayList<WordExample>();
    Context _context = null;
    ViewPager _viewPager = null;
    PagerAdapter _pagerAdapter = null;
    SearchView _searchView = null;
    String _query = "dictionary";
    public final static String _BuildConfig = BuildConfig.DEBUG ? "debug" : "release";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = this;

        // Enable home button
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Prepare UI
        setContentView(R.layout.activity_main);
        prepareSearchView();
        prepareContentView();

        // Display ads in release configurations
        if (_BuildConfig != "debug") {
            showAds();
        }

        // Search by intent (if any)
        Intent intent = getIntent();
        ProcessIntent(intent);
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

    private void prepareSearchView() {
        _searchView = (SearchView) findViewById(R.id.search_view);
        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                _query = query;
                Handler handler = new Handler(_context.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.welcomeText).setVisibility(View.GONE);
                        findViewById(R.id.view_pager_examples).setVisibility(View.GONE);
                        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                        if (_pagerAdapter != null) {
                            prepareExamplesList();
                        }
                    }
                });
                return false;
            }
        });
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
        _searchView.setQuery(intentQuery, true);
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

    private void prepareExamplesList() {
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
