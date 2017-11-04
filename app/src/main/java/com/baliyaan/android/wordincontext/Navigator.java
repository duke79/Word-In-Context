package com.baliyaan.android.wordincontext;

import android.app.Activity;
import android.content.Context;

import com.baliyaan.android.wordincontext.Components.Examples.MVP.ExamplesMVPContract;
import com.baliyaan.android.wordincontext.Components.Examples.MVP.ExamplesMVPViewPort;
import com.baliyaan.android.wordincontext.Components.SearchBox.MVP.SearchBoxMVPContract;
import com.baliyaan.android.wordincontext.Components.SearchBox.MVP.SearchBoxMVPViewPort;

/**
 * Created by Pulkit Singh on 11/4/2017.
 */

public class Navigator implements SearchBoxMVPContract.Navigator, ExamplesMVPContract.Navigator {

    /*
    * Member Variables
     */
    //MVP-Ports
    private SearchBoxMVPContract.MVPPort _searchPort = null;
    private ExamplesMVPContract.MVPPort _examplesPort = null;
    //Local-Use-Vars
    Activity _context = null;
    String _query = "dictionary";

    /*
    * Initialization
     */
    Navigator(Activity activity) {
        _context = activity;
        _searchPort = new SearchBoxMVPViewPort(_context, this);
        _examplesPort = new ExamplesMVPViewPort(_context, this);
    }

    /*
    * Methods to be called from parent activity
     */
    void onStop() {
        _examplesPort.onSaveState();
        _searchPort.onSaveState();
    }

    void onStart() {
        _examplesPort.onResumeState();
        _searchPort.onResumeState();
    }

    void onSearchIntent(String query) {
        _searchPort.setQuery(query);
    }

    /*
    * Navigator implementations
    */
    @Override //SearchBoxMVPContract.Navigator
    public void onSearchBoxSubmit(String query) {
        _query = query;
        _examplesPort.onQueryTextSubmit(query);
    }

    @Override
    public String getQuery() {
        return _query;
    }

    @Override
    public Context getContext() {
        return _context;
    }

    @Override //SearchBoxMVPContract.Navigator
    public void onTryAgain() {
        _examplesPort.onQueryTextSubmit(_query);
    }
}
