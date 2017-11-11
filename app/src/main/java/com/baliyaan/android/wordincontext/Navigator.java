package com.baliyaan.android.wordincontext;

import android.app.Activity;
import android.os.Bundle;

import com.baliyaan.android.mvp.Adapters.MVPNavigatorAdapter;
import com.baliyaan.android.wordincontext.Components.Definition.MVP.DefinitionMVPContract;
import com.baliyaan.android.wordincontext.Components.Definition.MVP.DefinitionMVPViewPort;
import com.baliyaan.android.wordincontext.Components.Examples.MVP.ExamplesMVPContract;
import com.baliyaan.android.wordincontext.Components.Examples.MVP.ExamplesMVPViewPort;
import com.baliyaan.android.wordincontext.Components.SearchBox.MVP.SearchBoxMVPContract;
import com.baliyaan.android.wordincontext.Components.SearchBox.MVP.SearchBoxMVPViewPort;

/**
 * Created by Pulkit Singh on 11/4/2017.
 */

public class Navigator extends MVPNavigatorAdapter implements SearchBoxMVPContract.Navigator, ExamplesMVPContract.Navigator, DefinitionMVPContract.Navigator{

    /*
    * Member Variables
     */
    //MVP-Ports
    private SearchBoxMVPContract.MVPPort _searchPort = null;
    private ExamplesMVPContract.Port _examplesPort = null;
    private DefinitionMVPViewPort _definitionPort = null;
    //Local-Use-Vars
    private String _query = "dictionary";

/*
* Initialization
 */
    Navigator(Activity activity) {
        super(activity);
        _searchPort = new SearchBoxMVPViewPort(this);
        _examplesPort = new ExamplesMVPViewPort(this);
        _definitionPort = new DefinitionMVPViewPort(this);
    }

/*
* Methods to be called from parent activity
 */
    void onSaveState(Bundle state) {
        _examplesPort.onSaveState(state);
        _searchPort.onSaveState(state);
        _definitionPort.onSaveState(state);
    }

    void onRestoreState(Bundle state) {
        _examplesPort.onRestoreState(state);
        _searchPort.onRestoreState(state);
        _definitionPort.onRestoreState(state);
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
        _definitionPort.onQueryTextSubmit(query);
    }

    @Override //ExamplesMVPContract.Navigator
    public String getQuery() {
        return _query;
    }

    @Override //SearchBoxMVPContract.Navigator
    public void onTryAgain() {
        onSearchBoxSubmit(_query);
    }
}
