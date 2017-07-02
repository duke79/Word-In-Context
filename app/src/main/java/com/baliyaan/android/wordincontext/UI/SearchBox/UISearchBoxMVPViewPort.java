package com.baliyaan.android.wordincontext.UI.SearchBox;

import android.app.Activity;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.UI.MVPViewPortAdapter;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class UISearchBoxMVPViewPort extends MVPViewPortAdapter<UISearchBoxMVPContract.Navigator,UISearchBoxMVPContract.Presenter> implements UISearchBoxMVPContract.View,UISearchBoxMVPContract.Port{
    private SearchView _searchView = null;

    public UISearchBoxMVPViewPort(Activity activity, UISearchBoxMVPContract.Navigator navigator){
        super(activity,navigator);
        super.bindPresenter(new UISearchBoxMVPPresenter(activity(),this));

        _searchView = (SearchView) activity().findViewById(R.id.search_view);
        _searchView.setOnQueryTextListener(presenter());
        _searchView.setOnSuggestionListener(presenter());
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
        activity().findViewById(R.id.welcomeText).setVisibility(View.GONE);
        activity().findViewById(R.id.view_pager_examples).setVisibility(View.GONE);
        activity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        if (navigator() != null) {
            navigator().onSearchBoxSubmit(query);
        }
    }
}
