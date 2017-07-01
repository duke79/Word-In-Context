package com.baliyaan.android.wordincontext.UI.SearchBox;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.baliyaan.android.wordincontext.Navigator;
import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class UISearchBoxView implements UISearchBoxContract.View,UISearchBoxContract.Port{
    private final UISearchBoxContract.Presenter _presenter;
    private Activity _activity = null;
    private Navigator _navigator = null;

    private SearchView _searchView = null;
    private String _query = null;

    public UISearchBoxView(Activity activity, Navigator navigator){
        _activity = activity;
        _navigator = navigator;
        _presenter = new UISearchBoxPresenter(_activity,this);

        _searchView = (SearchView) _activity.findViewById(R.id.search_view);
        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return _presenter.onQueryTextChange(newText);
            }

            @Override
            public boolean onQueryTextSubmit(final String query) {
                _query = query;

                // Remove suggestions
                _searchView.setSuggestionsAdapter(null);

                //Handle search
                Handler handler = new Handler(_activity.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        _activity.findViewById(R.id.welcomeText).setVisibility(View.GONE);
                        _activity.findViewById(R.id.view_pager_examples).setVisibility(View.GONE);
                        _activity.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                        if (_navigator != null) {
                            _navigator.prepareExamplesList(query);
                        }
                    }
                });
                return false;
            }
        });
        _searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return _presenter.onSuggestionSelect(position);
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return _presenter.onSuggestionClick(position);
            }
        });
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
}
