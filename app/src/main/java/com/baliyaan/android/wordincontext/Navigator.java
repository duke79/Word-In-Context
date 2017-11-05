package com.baliyaan.android.wordincontext;

import android.app.Activity;

import com.baliyaan.android.mvp.Adapters.MVPNavigatorAdapter;
import com.baliyaan.android.wordincontext.Components.Examples.MVP.ExamplesMVPContract;
import com.baliyaan.android.wordincontext.Components.Examples.MVP.ExamplesMVPViewPort;
import com.baliyaan.android.wordincontext.Components.SearchBox.MVP.SearchBoxMVPContract;
import com.baliyaan.android.wordincontext.Components.SearchBox.MVP.SearchBoxMVPViewPort;

/**
 * Created by Pulkit Singh on 11/4/2017.
 */

public class Navigator extends MVPNavigatorAdapter implements SearchBoxMVPContract.Navigator, ExamplesMVPContract.Navigator{

/*
* Member Variables
 */
    //MVP-Ports
    private SearchBoxMVPContract.MVPPort _searchPort = null;
    private ExamplesMVPContract.MVPPort _examplesPort = null;
    //Local-Use-Vars
    private String _query = "dictionary";

/*
* Initialization
 */
    Navigator(Activity activity) {
        super(activity);
        _searchPort = new SearchBoxMVPViewPort(this);
        _examplesPort = new ExamplesMVPViewPort(this);
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

    @Override //SearchBoxMVPContract.Navigator
    public void onTryAgain() {
        _examplesPort.onQueryTextSubmit(_query);
    }
}