package com.baliyaan.android.wordincontext.Components.SearchBox.MVP;

import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;

import com.baliyaan.android.wordincontext.MVPInfra.Interfaces.BaseMVPPort;
import com.baliyaan.android.wordincontext.MVPInfra.Interfaces.BaseMVPPresenter;
import com.baliyaan.android.wordincontext.MVPInfra.Interfaces.BaseMVPView;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public interface UISearchBoxMVPContract {
    interface MVPView extends BaseMVPView<MVPPresenter> {
        void setSuggestionsAdapter(CursorAdapter adapter);
        void clearFocus();
        void setQuery(String query, boolean b);
        void onQueryTextSubmit(String query);
    }

    interface MVPPresenter extends BaseMVPPresenter,SearchView.OnQueryTextListener,SearchView.OnSuggestionListener{

    }

    interface Navigator {
        void onSearchBoxSubmit(String query);
    }

    interface MVPPort extends BaseMVPPort {
        void setQuery(String query);
    }
}
