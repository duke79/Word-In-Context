package com.baliyaan.android.wordincontext;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.baliyaan.android.library.ads.Interstitial;
import com.baliyaan.android.mvp.Adapters.MVPNavigatorAdapter;
import com.baliyaan.android.wordincontext.Components.SearchBox.MVP.Contract;
import com.baliyaan.android.wordincontext.Components.SearchBox.MVP.ViewPort;

public class MainActivity extends AppCompatActivity {

    Navigator _navigator = null;
    Interstitial _interstitial = null;
    Context _context = null;

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

        /*
        * Prepare UI
        */
        setContentView(R.layout.activity_main);
        _navigator = new Navigator(this);
        ImageView bgImage = (ImageView) findViewById(R.id.background_blur);
        // Back-ground image
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.background_mage);
        bgImage.setImageBitmap(Bitmap.createBitmap(b));

        /*
        * Search by intent (if any)
        */
        Intent intent = getIntent();
        onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    public class Navigator
        extends MVPNavigatorAdapter
        implements com.baliyaan.android.wordincontext.Components.SearchBox.MVP.Contract.Navigator {

        Contract.Port _searchPort = null;
        private String _query;

        protected Navigator(Context context) {
            super(context);
            _searchPort = new ViewPort(this);
        }

        /*
        * Methods to be called from parent activity
        */
        void onSaveState(Bundle state) {
            state.putString("query", _query);
            _searchPort.onSaveState(state);
        }

        void onRestoreState(Bundle state) {
            _query = (String) state.get("query");
            _searchPort.onRestoreState(state);
        }

        /**
         * Simulates search operation.
         * To be called from parent activity, specifically, to handle search intent from other activities.
         * @param query word string to search
         */
        void onSearchIntent(String query) {
            onSearchBoxSubmit(query);
        }

        /*
        * Methods to be called from ports
        */
        /**
         * To be called from SearchBox.MVP.Contract.Port, once query is submitted from SearchBox
         *
         * @param query word string to search
         */
        @Override //Contract.Navigator
        public void onSearchBoxSubmit(String query) {

            Intent intent = new Intent(getContext(), WordDictActivity.class);
            intent.putExtra("query", query);
            getContext().startActivity(intent);
        }
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