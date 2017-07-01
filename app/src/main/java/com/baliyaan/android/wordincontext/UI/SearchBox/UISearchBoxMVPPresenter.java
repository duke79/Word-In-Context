package com.baliyaan.android.wordincontext.UI.SearchBox;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.baliyaan.android.wordincontext.Model.Dictionary;
import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.UI.MVPPresenterAdapter;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

class UISearchBoxMVPPresenter extends MVPPresenterAdapter<UISearchBoxMVPContract.View> implements UISearchBoxMVPContract.Presenter{
    private Dictionary _dictionary = null;
    private Dictionary.SuggestionsAdapter _adapter = null;

    UISearchBoxMVPPresenter(Activity activity, UISearchBoxMVPContract.View view){
        super(activity, view);
        //Load dictionary
        _dictionary = Dictionary.GetInstance(activity());
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
                activity().findViewById(R.id.welcomeText).setVisibility(View.GONE);
                activity().findViewById(R.id.view_pager_examples).setVisibility(View.GONE);
                activity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                view().onQueryTextSubmit(query);
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i("TextChange", "=(" + newText + ")");

        if (newText == null || newText.isEmpty()) {
            view().setSuggestionsAdapter(null);
        } else {
            _adapter = _dictionary.GetSuggestionsAdapter(activity());
            //adapter.RemoveCursor();
            view().setSuggestionsAdapter(_adapter);
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
        view().setQuery(suggestion,true);
        view().clearFocus();
        return true;
    }
}
