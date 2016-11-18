package com.baliyaan.android.wordincontext;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static com.baliyaan.android.wordincontext.Scraper.GetExamples;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "ContextDictionary";
    ArrayList<WordExample> _examples = new ArrayList<WordExample>();
    Context _context = null;
    RecyclerView _recyclerView = null;
    ExamplesAdapter _examplesAdapter = null;
    SearchView _searchView = null;
    String _query = "dictionary";
    public final static String _BuildConfig = BuildConfig.DEBUG ? "debug" : "release";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = this;

        // Enable home button
        ActionBar actionBar = getActionBar();
        if(null != actionBar)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Prepare UI
        setContentView(R.layout.activity_main);
        prepareSearchView();
        prepareContentView();

        // Display ads in release configurations
        if(_BuildConfig != "debug") {
            Interstitial interstitialAd = new Interstitial(_context, "ca-app-pub-4278963888720323/8594194494");
            interstitialAd.showEvery(300000, true); // every 5 minutes
        }

        // Search by intent (if any)
        Intent intent = getIntent();
        ProcessIntent(intent);
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
                        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                        if(_examplesAdapter != null) {
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
        if(clipData == null)
            return;
        ClipData.Item item = clipData.getItemAt(0);
        if(item == null)
            return;
        String intentQuery = item.getText().toString();
        if(intentQuery == null)
            return;
        if(!(intentQuery.length()>0))
            return;
        _searchView.setQuery(intentQuery,true);
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
        _recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        _examplesAdapter = new ExamplesAdapter(this,_examples);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(_context,LinearLayoutManager.HORIZONTAL,false);
        if(null != _recyclerView) {
            _recyclerView.setLayoutManager(mLayoutManager);
            _recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            _recyclerView.setAdapter(_examplesAdapter);
        }
    }

    private void prepareExamplesList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GetExamples(_query,_examples);
                    Handler handler = new Handler(_context.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(_examplesAdapter != null) {
                                findViewById(R.id.progressBar).setVisibility(View.GONE);
                                findViewById(R.id.recycler_view).setVisibility(View.VISIBLE);
                                _examplesAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();

                    ((MainActivity)_context).runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(_context, "No internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
