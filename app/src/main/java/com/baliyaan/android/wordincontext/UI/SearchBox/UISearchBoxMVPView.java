package com.baliyaan.android.wordincontext.UI.SearchBox;

import android.app.Activity;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;

import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.UI.MVPViewAdapter;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class UISearchBoxMVPView extends MVPViewAdapter<UISearchBoxMVPContract.Navigator> implements UISearchBoxMVPContract.View,UISearchBoxMVPContract.Port{
    private UISearchBoxMVPContract.Presenter _presenter;
    private SearchView _searchView = null;

    public UISearchBoxMVPView(Activity activity, UISearchBoxMVPContract.Navigator navigator){
        super(activity,navigator);
        _presenter = new UISearchBoxMVPPresenter(activity(),this);

        _searchView = (SearchView) activity().findViewById(R.id.search_view);
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
        if (navigator() != null) {
            navigator().onSearchBoxSubmit(query);
        }
    }
}