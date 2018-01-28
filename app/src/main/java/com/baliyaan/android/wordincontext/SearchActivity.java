package com.baliyaan.android.wordincontext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baliyaan.android.mvp.Adapters.MVPNavigatorAdapter;
import com.baliyaan.android.wordincontext.Components.SearchBox.MVP.Contract;
import com.baliyaan.android.wordincontext.Components.SearchBox.MVP.ViewPort;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Pulkit Singh on 1/23/2018.
 */

public class SearchActivity extends AppCompatActivity {
    public class Navigator
            extends MVPNavigatorAdapter
            implements com.baliyaan.android.wordincontext.Components.SearchBox.MVP.Contract.Navigator {

        Contract.Port _searchPort = null;
        private String _query;

        protected Navigator(Context context) {
            super(context);
            View view = findViewById(R.id.search_view);
            _searchPort = new ViewPort(this, view);
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

    private FirebaseAnalytics _firebaseAnalytics;
    private Navigator _navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Firebase */
        // Obtain the FirebaseAnalytics instance.
        _firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        /*Set content view*/
        setContentView(R.layout.activity_search);

        /*Initialize MVP Navigator*/
        _navigator = new SearchActivity.Navigator(this);
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
