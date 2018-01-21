package com.baliyaan.android.wordincontext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.baliyaan.android.mvp.Adapters.MVPNavigatorAdapter;
import com.baliyaan.android.wordincontext.Components.Definition.MVP.Contract;
import com.baliyaan.android.wordincontext.Components.Examples.MVP.ViewPort;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Pulkit Singh on 1/10/2018.
 */

public class WordDictActivity
        extends AppCompatActivity {

    private Navigator _navigator;
    private FirebaseAnalytics _firebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Firebase */
        // Obtain the FirebaseAnalytics instance.
        _firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        /*Set content view*/
        setContentView(R.layout.activity_word_dict);

        /*Initialize MVP Navigator*/
        _navigator = new Navigator(this);

        /*
        * Search by intent (if any)
        */
        Intent intent = getIntent();
        onNewIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /*Handle search intent - exmaples & definition views*/
        String query = intent.getStringExtra("query");
        _navigator.onSearchIntent(query);
        /*Log query in firebase*/
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, query);
        if (null != _firebaseAnalytics)
            _firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
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
            extends
            MVPNavigatorAdapter
            implements
            com.baliyaan.android.wordincontext.Components.Examples.MVP.Contract.Navigator,
            com.baliyaan.android.wordincontext.Components.Definition.MVP.Contract.Navigator {

        /*
        * Member Variables
         */
        //MVP-Ports
        private com.baliyaan.android.wordincontext.Components.Examples.MVP.Contract.Port _examplesPort = null;
        private Contract.Port _definitionPort = null;
        //Local-Use-Vars
        private String _query = "";

        /*
        * Initialization
         */
        Navigator(Activity activity) {
            super(activity);
            _examplesPort = new ViewPort(this);
            _definitionPort = new com.baliyaan.android.wordincontext.Components.Definition.MVP.ViewPort(this);
        }

        /*
        * Methods to be called from parent activity
         */
        void onSaveState(Bundle state) {
            state.putString("query", _query);
            _examplesPort.onSaveState(state);
            _definitionPort.onSaveState(state);
        }

        void onRestoreState(Bundle state) {
            _query = (String) state.get("query");
            _examplesPort.onRestoreState(state);
            _definitionPort.onRestoreState(state);
        }

        /**
         * Simulates search operation.
         * To be called from parent activity, specifically, to handle search intent from other activities.
         *
         * @param query word string to search
         */
        void onSearchIntent(String query) {
            _query = query;
            _examplesPort.onQueryTextSubmit(query);
            _definitionPort.onQueryTextSubmit(query);

            /*Update toolbar*/
            String firstChar = query.substring(0, 1);
            firstChar = firstChar.toUpperCase();
            query = firstChar + query.substring(1, query.length()).toLowerCase();

            Toolbar toolbar = (Toolbar) findViewById(R.id.word_dict_toolbar);
            if (null != toolbar)
                toolbar.setTitle(query);
        }

    /*
    * Navigator implementations
    */

        @Override //Contract.Navigator
        public String getQuery() {
            return _query;
        }

        /**
         * Re-load the contents for last query.
         * Mainly in cases when content couldn't be loaded due to some reason (NoInternet etc.).
         */
        @Override //Contract.Navigator
        public void onTryAgain() {
            onSearchIntent(_query);
        }
    }
}
