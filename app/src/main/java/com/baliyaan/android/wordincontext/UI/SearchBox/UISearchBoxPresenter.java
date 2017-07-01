package com.baliyaan.android.wordincontext.UI.SearchBox;

import android.app.Activity;
import android.util.Log;

import com.baliyaan.android.wordincontext.Model.Dictionary;

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
