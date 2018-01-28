package com.baliyaan.android.wordincontext.Components.Definition.MVP;

import android.os.Bundle;
import android.view.View;

import com.baliyaan.android.mvp.Adapters.MVPViewPortAdapter;
import com.baliyaan.android.wordincontext.Components.Definition.UI.CustomView;
import com.baliyaan.android.wordincontext.Model.Definition;
import com.baliyaan.android.wordincontext.R;

import java.util.ArrayList;

/**
 * Created by Pulkit Singh on 11/5/2017.
 */

public class ViewPort
        extends MVPViewPortAdapter<Contract.Navigator, Contract.Presenter>
        implements Contract.View, Contract.Port {
    private ArrayList<Definition> _definitions;

    public ViewPort(Contract.Navigator navigator, View view) {
        super(navigator,view);
        super.bindPresenter(new Presenter(this));
        init();
    }

    private void init() {
        if (null != view())
            view().setVisibility(View.GONE);
    }

    @Override
    public void setDefinitions(ArrayList<Definition> definitions) {
        _definitions = definitions;

        if (null != view()) {
            if (_definitions.size() < 1) {
                ((CustomView) view()).addDefinition(navigator().getContext().getString(R.string.no_defintion));
            }
            else{
                for(Definition definition : _definitions)
                    ((CustomView) view()).addDefinition(definition._definition);
            }

            view().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveState(Bundle state) {
        super.onSaveState(state);
        state.putParcelableArrayList("definitions", _definitions);
    }

    @Override
    public void onRestoreState(Bundle state) {
        super.onRestoreState(state);
        ArrayList<Definition> definitions = state.getParcelableArrayList("definitions");
        setDefinitions(definitions);
    }

    @Override
    public void onQueryTextSubmit(String query) {
        if (view() != null) {
            view().setVisibility(View.GONE);
            presenter().onQueryTextSubmit(query);
        }
    }
}
