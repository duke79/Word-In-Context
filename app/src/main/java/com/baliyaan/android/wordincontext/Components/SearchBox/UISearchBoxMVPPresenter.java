package com.baliyaan.android.wordincontext.Components.SearchBox;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.baliyaan.android.wordincontext.Data.Autocomplete.SuggestionsAdapter;
import com.baliyaan.android.wordincontext.MVPInfra.MVPPresenterAdapter;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

class UISearchBoxMVPPresenter extends MVPPresenterAdapter<UISearchBoxMVPContract.View> implements UISearchBoxMVPContract.Presenter{
    private SuggestionsAdapter _adapter = null;

    UISearchBoxMVPPresenter(Activity activity, UISearchBoxMVPContract.View view){
        super(activity, view);

        _adapter = SuggestionsAdapter.getInstance(activity());
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        // Remove suggestions
        view().setSuggestionsAdapter(null);

        //Handle search
        Handler handler = new Handler(activity().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                view().onQueryTextSubmit(query);
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        Log.i("TextChange", "=(" + newText + ")");

        if (newText == null || newText.isEmpty()) {
            view().setSuggestionsAdapter(null);
        } else {
            view().setSuggestionsAdapter(_adapter);
            _adapter.suggestFor(newText,5);
        }
        return true;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        String suggestion = _adapter.getSuggestionAt(position);
        view().setQuery(suggestion,true);
        view().clearFocus();
        return true;
    }
}
