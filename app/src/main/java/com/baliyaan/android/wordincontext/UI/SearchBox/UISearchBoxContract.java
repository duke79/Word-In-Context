package com.baliyaan.android.wordincontext.UI.SearchBox;

import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public interface UISearchBoxContract {
    interface View{
        void setSuggestionsAdapter(CursorAdapter adapter);
        void clearFocus();
        void setQuery(String query, boolean b);
        void onQueryTextSubmit(String query);
    }

    interface Presenter extends SearchView.OnQueryTextListener,SearchView.OnSuggestionListener{

    }

    interface Navigator {
        void onSearchBoxSubmit(String query);
    }

    interface Port{
        void setQuery(String query);
    }
}
