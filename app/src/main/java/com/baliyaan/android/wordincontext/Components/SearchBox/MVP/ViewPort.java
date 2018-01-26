package com.baliyaan.android.wordincontext.Components.SearchBox.MVP;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baliyaan.android.mvp.Adapters.MVPViewPortAdapter;
import com.baliyaan.android.wordincontext.R;


/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class ViewPort extends MVPViewPortAdapter<Contract.Navigator, Contract.Presenter> implements Contract.View, Contract.Port {
    private final ListView _suggestionsView;
    private SearchView _searchView = null;

    public ViewPort(Contract.Navigator navigator) {
        super(navigator);
        super.bindPresenter(new Presenter(this));

        /*Configure Search View*/
        _searchView = (SearchView) ((Activity) navigator().getContext()).findViewById(R.id.search_view);
        if (null != _searchView) {
            _searchView.setOnQueryTextListener(presenter());
            _searchView.setOnSuggestionListener(presenter());
            //_searchView.requestFocus(0,null);
            //_searchView.requestFocusFromTouch();
            _searchView.setIconified(false);
        }

        //SearchView icon color
        /*ImageView searchViewIcon = (ImageView)_searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            searchViewIcon.setImageTintList(ColorStateList.valueOf(0)); //black-color
        }*/

        /*Configure Suggestions View*/
        _suggestionsView = (ListView) ((Activity) navigator().getContext()).findViewById(R.id.suggestions_view);
        if(null != _suggestionsView){
            _suggestionsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //String strSuggestion = (String) ((TextView)view).getText();
                    presenter().onSuggestionClick(position);
                }
            });
        }
    }


    @Override
    public void setQuery(String query) {
        if (null != _searchView)
            _searchView.setQuery(query, true);
    }

    @Override
    public void setSuggestionsAdapter(CursorAdapter adapter) {
        if(null != _suggestionsView) {
            _suggestionsView.setAdapter(adapter);
        }
        else if (null != _searchView){
            _searchView.setSuggestionsAdapter(adapter);
        }
    }

    @Override
    public void clearFocus() {
        if (null != _searchView)
            _searchView.clearFocus();
    }

    @Override
    public void setQuery(String query, boolean b) {
        if (null != _searchView)
            _searchView.setQuery(query, b);
    }

    @Override
    public void onQueryTextSubmit(String query) {
        if (navigator() != null) {
            navigator().onSearchBoxSubmit(query);
            clearFocus();
        }
    }

    @Override
    public void onRestoreState(Bundle state) {
        super.onRestoreState(state);
        clearFocus();
    }
}
