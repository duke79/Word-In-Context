package com.baliyaan.android.wordincontext.Components.SearchBox.MVP;


import android.os.Handler;
import android.util.Log;

import com.baliyaan.android.mvp.Adapters.MVPPresenterAdapter;
import com.baliyaan.android.wordincontext.Components.SearchBox.Data.Autocomplete.SuggestionsAdapter;
import com.baliyaan.android.wordincontext.R;


/**
 * Created by Pulkit Singh on 7/1/2017.
 */

class Presenter extends MVPPresenterAdapter<Contract.View> implements Contract.Presenter {
    private SuggestionsAdapter _adapter = null;

    Presenter(Contract.View view){
        super(view);

        _adapter = SuggestionsAdapter.getInstance(view().getContext());
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        // Remove suggestions
        view().setSuggestionsAdapter(null);

        //Handle search
        Handler handler = new Handler((view().getContext()).getMainLooper());
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
            _adapter.suggestFor(newText, Integer.parseInt(view().getContext().getString(R.string.nbr_search_suggestions)));
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
