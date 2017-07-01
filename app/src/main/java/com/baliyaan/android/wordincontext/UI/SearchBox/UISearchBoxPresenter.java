package com.baliyaan.android.wordincontext.UI.SearchBox;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.baliyaan.android.wordincontext.Model.Dictionary;
import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class UISearchBoxPresenter implements UISearchBoxContract.Presenter{
    private Dictionary _dictionary = null;
    private Activity _activity = null;
    private UISearchBoxContract.View _view = null;
    private Dictionary.SuggestionsAdapter _adapter = null;

    public UISearchBoxPresenter(Activity activity, UISearchBoxContract.View view){
        _activity = activity;
        _view = view;
        //Load dictionary
        _dictionary = Dictionary.GetInstance(_activity);
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        // Remove suggestions
        _view.setSuggestionsAdapter(null);

        //Handle search
        Handler handler = new Handler(_activity.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                _activity.findViewById(R.id.welcomeText).setVisibility(View.GONE);
                _activity.findViewById(R.id.view_pager_examples).setVisibility(View.GONE);
                _activity.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                _view.onQueryTextSubmit(query);
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i("TextChange", "=(" + newText + ")");

        if (newText == null || newText.isEmpty()) {
            _view.setSuggestionsAdapter(null);
        } else {
            _adapter = _dictionary.GetSuggestionsAdapter(_activity);
            //adapter.RemoveCursor();
            _view.setSuggestionsAdapter(_adapter);
            _adapter.SuggestFor(newText,5);
        }
        return true;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        String suggestion = _adapter.GetSuggestionAt(position);
        _view.setQuery(suggestion,true);
        _view.clearFocus();
        return true;
    }
}
