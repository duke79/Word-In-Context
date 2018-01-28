package com.baliyaan.android.wordincontext.Components.SearchBox.MVP;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baliyaan.android.mvp.Adapters.MVPViewPortAdapter;
import com.baliyaan.android.wordincontext.R;


/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class ViewPort extends MVPViewPortAdapter<Contract.Navigator, Contract.Presenter> implements Contract.View, Contract.Port {
    private final ListView _suggestionsView;

    public ViewPort(Contract.Navigator navigator, View view) {
        super(navigator, view);
        super.bindPresenter(new Presenter(this));

        /*Configure Search View*/
        if (null != view()) {
            ((SearchView)view()).setOnQueryTextListener(presenter());
            ((SearchView)view()).setOnSuggestionListener(presenter());
            //_searchView.requestFocus(0,null);
            //_searchView.requestFocusFromTouch();
            ((SearchView)view()).setIconified(false);
        }

        //SearchView icon color
        /*ImageView searchViewIcon = (ImageView)_searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            searchViewIcon.setImageTintList(ColorStateList.valueOf(0)); //black-color
        }*/

        /*Configure Suggestions View*/
        _suggestionsView = (ListView) ((Activity) navigator().getContext()).findViewById(R.id.suggestions_view);
        if (null != _suggestionsView) {
            _suggestionsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //String strSuggestion = (String) ((TextView)view).getText();
                    presenter().onSuggestionClick(position);
                }
            });
        }
    }


    @Override
    public void setQuery(String query) {
        if (null != view())
            ((SearchView)view()).setQuery(query, true);
    }

    @Override
    public void setSuggestionsAdapter(CursorAdapter adapter) {
        if (null != _suggestionsView) {
            _suggestionsView.setAdapter(adapter);
        } else if (null != view()) {
            ((SearchView)view()).setSuggestionsAdapter(adapter);
        }
    }

    @Override
    public void clearFocus() {
        if (null != view())
            ((SearchView)view()).clearFocus();
    }

    @Override
    public void setQuery(String query, boolean b) {
        if (null != view())
            ((SearchView)view()).setQuery(query, b);
    }

    @Override
    public void onQueryTextSubmit(String query) {
        if (navigator() != null) {
            navigator().onSearchBoxSubmit(query);
            clearFocus();
        }
    }

    @Override
    public void onRestoreState(Bundle state) {
        super.onRestoreState(state);
        clearFocus();
    }
}
