package com.baliyaan.android.wordincontext.UI.SearchBox;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.baliyaan.android.wordincontext.Model.Dictionary;
import com.baliyaan.android.wordincontext.Navigator;
import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class UISearchBoxView implements UISearchBoxContract.View,UISearchBoxContract.Port{
    private Dictionary _dictionary;
    private Activity _activity = null;
    private Navigator _navigator = null;

    private SearchView _searchView = null;
    private String _query = null;

    public UISearchBoxView(Activity activity, Navigator navigator){
        _activity = activity;
        _navigator = navigator;
        //Load dictionary
        _dictionary = Dictionary.GetInstance(_activity);

        _searchView = (SearchView) _activity.findViewById(R.id.search_view);
        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("TextChange", "=(" + newText + ")");

                if (newText == null || newText.isEmpty()) {
                    _searchView.setSuggestionsAdapter(null);
                } else {
                    final Dictionary.SuggestionsAdapter adapter = _dictionary.GetSuggestionsAdapter(_activity);
                    //adapter.RemoveCursor();
                    _searchView.setSuggestionsAdapter(adapter);
                    adapter.SuggestFor(newText,5);
                    _searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                        @Override
                        public boolean onSuggestionSelect(int position) {
                            return false;
                        }

                        @Override
                        public boolean onSuggestionClick(int position) {
                            String suggestion = adapter.GetSuggestionAt(position);
                            _searchView.setQuery(suggestion,true);
                            _searchView.clearFocus();
                            return true;
                        }
                    });
                }
                return true;
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
    }


    @Override
    public void setQuery(String query) {

    }
}
