package com.baliyaan.android.wordincontext.UI.SearchBox;

import android.app.Activity;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;

import com.baliyaan.android.wordincontext.Navigator;
import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class UISearchBoxView implements UISearchBoxContract.View,UISearchBoxContract.Port{
    private UISearchBoxContract.Presenter _presenter;
    private Activity _activity = null;
    private Navigator _navigator = null;
    private SearchView _searchView = null;

    public UISearchBoxView(Activity activity, Navigator navigator){
        _activity = activity;
        _navigator = navigator;
        _presenter = new UISearchBoxPresenter(_activity,this);

        _searchView = (SearchView) _activity.findViewById(R.id.search_view);
        _searchView.setOnQueryTextListener(_presenter);
        _searchView.setOnSuggestionListener(_presenter);
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
        if (_navigator != null) {
            _navigator.prepareExamplesList(query);
        }
    }
}
