package com.baliyaan.android.wordincontext;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.baliyaan.android.library.ads.Interstitial;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    Interstitial _interstitial = null;
    Context _context = null;
    private FirebaseAnalytics _firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = this;

        /* Firebase */
        // Obtain the FirebaseAnalytics instance.
        _firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        /*Enable home button*/
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*
        * Prepare UI
        */
        setContentView(R.layout.activity_main);
        // Back-ground image
        ImageView bgImage = (ImageView) findViewById(R.id.background_blur);
        if(null != bgImage) {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.background_mage);
            bgImage.setImageBitmap(Bitmap.createBitmap(b));
        }
        // SearchBox callback
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Show the word definition activity*/
                Intent intentSearch = new Intent(_context, SearchActivity.class);
                intentSearch.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                _context.startActivity(intentSearch);
                overridePendingTransition(0,0);
            }
        });
        //Navigation Drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        /*
        * Search by intent (if any)
        */
        Intent intent = getIntent();
        onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0,0); //To prevent animation on returning from SearchActivity

        /*
        * Display ads
        */
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null == _interstitial) {
                    _interstitial = new Interstitial(_context, getString(R.string.adId));
                    _interstitial.AllowOnlyAfter((long) 3.6e+5);// 6 minutes
                    _interstitial.AllowInDebug(false);
                    _interstitial.showEvery(1000000, true); // every 16.66 minutes
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

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

        /*Show the word definition activity*/
        Intent intentWordDict = new Intent(this, WordDictActivity.class);
        intentWordDict.putExtra("query", intentQuery);
        this.startActivity(intentWordDict);
    }
}


/*
Todo: Word List
Todo: Meaning and Examples
Todo: Could also gamify for better engagement and ease of learning
Todo: Example: https://play.google.com/store/apps/details?id=com.praveenj.gre
Todo: Translations
Todo: Antonyms-Synonyms
Todo: CamScanner
Todo: History
Todo: Save offline
Todo: MORE LANGUAGES - same app or more apps
Todo: Voice Search (from any screen, maybe with a keyword first like OK Google, short results preferably on overlay screen)
Todo: Text-to-Speech
Todo: More than 10 results
*/