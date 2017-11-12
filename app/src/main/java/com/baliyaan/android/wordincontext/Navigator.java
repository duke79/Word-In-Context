package com.baliyaan.android.wordincontext;

import android.app.Activity;
import android.os.Bundle;

import com.baliyaan.android.mvp.Adapters.MVPNavigatorAdapter;
import com.baliyaan.android.wordincontext.Components.Definition.MVP.Contract;
import com.baliyaan.android.wordincontext.Components.Examples.MVP.ViewPort;


/**
 * Created by Pulkit Singh on 11/4/2017.
 */

public class Navigator
        extends
        MVPNavigatorAdapter
        implements
        com.baliyaan.android.wordincontext.Components.SearchBox.MVP.Contract.Navigator,
        com.baliyaan.android.wordincontext.Components.Examples.MVP.Contract.Navigator,
        com.baliyaan.android.wordincontext.Components.Definition.MVP.Contract.Navigator {

    /*
    * Member Variables
     */
    //MVP-Ports
    private com.baliyaan.android.wordincontext.Components.SearchBox.MVP.Contract.Port _searchPort = null;
    private com.baliyaan.android.wordincontext.Components.Examples.MVP.Contract.Port _examplesPort = null;
    private Contract.Port _definitionPort = null;
    //Local-Use-Vars
    private String _query = "";

    /*
    * Initialization
     */
    Navigator(Activity activity) {
        super(activity);
        _searchPort = new com.baliyaan.android.wordincontext.Components.SearchBox.MVP.ViewPort(this);
        _examplesPort = new ViewPort(this);
        _definitionPort = new com.baliyaan.android.wordincontext.Components.Definition.MVP.ViewPort(this);
    }

    /*
    * Methods to be called from parent activity
     */
    void onSaveState(Bundle state) {
        state.putString("query",_query);
        _examplesPort.onSaveState(state);
        _searchPort.onSaveState(state);
        _definitionPort.onSaveState(state);
    }

    void onRestoreState(Bundle state) {
        _query = (String) state.get("query");
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
    @Override //Contract.Navigator
    public void onSearchBoxSubmit(String query) {
        _query = query;
        _examplesPort.onQueryTextSubmit(query);
        _definitionPort.onQueryTextSubmit(query);
    }

    @Override //Contract.Navigator
    public String getQuery() {
        return _query;
    }

    @Override //Contract.Navigator
    public void onTryAgain() {
        onSearchBoxSubmit(_query);
    }
}
