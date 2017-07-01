package com.baliyaan.android.wordincontext.UI.SearchBox;

import android.support.v4.widget.CursorAdapter;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public interface UISearchBoxContract {
    interface View{
        void setSuggestionsAdapter(CursorAdapter adapter);
        void clearFocus();
        void setQuery(String query, boolean b);
    }

    interface Presenter {
        boolean onQueryTextChange(String newText);
        public boolean onSuggestionSelect(int position);
        public boolean onSuggestionClick(int position);
    }

    interface Port{
        void setQuery(String query);
    }
}
