package com.baliyaan.android.wordincontext.UI.SearchBox;

import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;

import com.baliyaan.android.wordincontext.UI.MVPBasePortInterface;
import com.baliyaan.android.wordincontext.UI.MVPBasePresenterInterface;
import com.baliyaan.android.wordincontext.UI.MVPBaseViewInterface;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public interface UISearchBoxMVPContract {
    interface View extends MVPBaseViewInterface {
        void setSuggestionsAdapter(CursorAdapter adapter);
        void clearFocus();
        void setQuery(String query, boolean b);
        void onQueryTextSubmit(String query);
    }

    interface Presenter extends MVPBasePresenterInterface,SearchView.OnQueryTextListener,SearchView.OnSuggestionListener{

    }

    interface Navigator {
        void onSearchBoxSubmit(String query);
    }

    interface Port extends MVPBasePortInterface{
        void setQuery(String query);
    }
}
