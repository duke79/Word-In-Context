package com.baliyaan.android.wordincontext.Components.SearchBox.MVP;

import android.app.Activity;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;

import com.baliyaan.android.wordincontext.MVPInfra.Adapters.MVPViewPortAdapter;
import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class SearchBoxMVPViewPort extends MVPViewPortAdapter<SearchBoxMVPContract.Navigator,SearchBoxMVPContract.MVPPresenter> implements SearchBoxMVPContract.MVPView,SearchBoxMVPContract.MVPPort {
    private SearchView _searchView = null;

    public SearchBoxMVPViewPort(Activity activity, SearchBoxMVPContract.Navigator navigator){
        super(activity,navigator);
        super.bindPresenter(new SearchBoxMVPPresenter(activity(),this));

        //Configure searchView
        _searchView = (SearchView) activity().findViewById(R.id.search_view);
        if(null!=_searchView) {
            _searchView.setOnQueryTextListener(presenter());
            _searchView.setOnSuggestionListener(presenter());
        }
    }


    @Override
    public void setQuery(String query) {
        _searchView.setQuery(query,true);
    }

    @Override
    public void setSuggestionsAdapter(CursorAdapter adapter) {
        _searchView.setSuggestionsAdapter(adapter);
    }

    @Override
    public void clearFocus() {
        _searchView.clearFocus();
    }

    @Override
    public void setQuery(String query, boolean b) {
        _searchView.setQuery(query,b);
    }

    @Override
    public void onQueryTextSubmit(String query) {
        if (navigator() != null) {
            navigator().onSearchBoxSubmit(query);
        }
    }
}
