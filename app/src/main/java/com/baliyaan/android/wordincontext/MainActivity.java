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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baliyaan.android.library.ads.Interstitial;
import com.baliyaan.android.wordincontext.Model.Dictionary;
import com.baliyaan.android.wordincontext.Model.WordExample;
import com.baliyaan.android.wordincontext.UI.ExamplesAdapter;

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
import static com.baliyaan.android.wordincontext.IO.Scraper.GetExamples;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "ContextDictionary";
    ArrayList<WordExample> _examples = new ArrayList<WordExample>();
    static Context _context = null;
    ViewPager _viewPager = null;
    PagerAdapter _pagerAdapter = null;
    SearchView _searchView = null;
    public String _query = "dictionary";
    Dictionary _dictionary = null;
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

    private void prepareSearchView() {
        _searchView = (SearchView) findViewById(R.id.search_view);
        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("TextChange", "=(" + newText + ")");

                if (newText == null || newText.isEmpty()) {
                    _searchView.setSuggestionsAdapter(null);
                } else {
                    final Dictionary.SuggestionsAdapter adapter = _dictionary.GetSuggestionsAdapter(_context);
                    //adapter.RemoveCursor();
                    _searchView.setSuggestionsAdapter(adapter);
                    adapter.SuggestFor(newText,5);
                    _searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                        @Override
                        public boolean onSuggestionSelect(int position) {
                            return false;
                        }

                        @Override
                        public boolean onSuggestionClick(int position) {
                            String suggestion = adapter.GetSuggestionAt(position);
                            _searchView.setQuery(suggestion,true);
                            _searchView.clearFocus();
                            return true;
                        }
                    });
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                _query = query;

                // Remove suggestions
                _searchView.setSuggestionsAdapter(null);

                //Handle search
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
