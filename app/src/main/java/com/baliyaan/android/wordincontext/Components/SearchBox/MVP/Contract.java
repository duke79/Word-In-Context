package com.baliyaan.android.wordincontext.Components.SearchBox.MVP;


import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;

import com.baliyaan.android.mvp.Interfaces.BaseMVPNavigator;
import com.baliyaan.android.mvp.Interfaces.BaseMVPPort;
import com.baliyaan.android.mvp.Interfaces.BaseMVPPresenter;
import com.baliyaan.android.mvp.Interfaces.BaseMVPView;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public interface Contract {
    interface View extends BaseMVPView<Presenter> {
        void setSuggestionsAdapter(CursorAdapter adapter);
        void clearFocus();
        void setQuery(String query, boolean b);
        void onQueryTextSubmit(String query);
    }

    interface Presenter extends BaseMVPPresenter,SearchView.OnQueryTextListener,SearchView.OnSuggestionListener{

    }

    interface Navigator extends BaseMVPNavigator {
        void onSearchBoxSubmit(String query);
    }

    interface Port extends BaseMVPPort {
        void setQuery(String query);
    }
}
