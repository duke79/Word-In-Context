package com.baliyaan.android.wordincontext.Components.SearchBox.MVP;


import android.app.Activity;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;

import com.baliyaan.android.mvp.Adapters.MVPViewPortAdapter;
import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class SearchBoxMVPViewPort extends MVPViewPortAdapter<SearchBoxMVPContract.Navigator,SearchBoxMVPContract.MVPPresenter> implements SearchBoxMVPContract.MVPView,SearchBoxMVPContract.MVPPort {
    private SearchView _searchView = null;

    public SearchBoxMVPViewPort(SearchBoxMVPContract.Navigator navigator){
        super(navigator);
        super.bindPresenter(new SearchBoxMVPPresenter(this));

        //Configure searchView
        _searchView = (SearchView) ((Activity)navigator().getContext()).findViewById(R.id.search_view);
        if(null!=_searchView) {
            _searchView.setOnQueryTextListener(presenter());
            _searchView.setOnSuggestionListener(presenter());
        }

        //SearchView icon color
        /*ImageView searchViewIcon = (ImageView)_searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            searchViewIcon.setImageTintList(ColorStateList.valueOf(0)); //black-color
        }*/
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
