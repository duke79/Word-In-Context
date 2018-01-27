package com.baliyaan.android.wordincontext.Components.Definition.MVP;

import android.app.Activity;
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
    private CustomView _view;
    private ArrayList<Definition> _definitions;

    public ViewPort(Contract.Navigator navigator) {
        super(navigator);
        super.bindPresenter(new Presenter(this));
        init();
    }

    private void init() {
        _view = (CustomView) ((Activity) navigator().getContext()).findViewById(R.id.definition_view);
        if (null != _view)
            _view.setVisibility(View.GONE);
    }

    @Override
    public void setDefinitions(ArrayList<Definition> definitions) {
        _definitions = definitions;

        if (null != _view) {
            if (_definitions.size() < 1) {
                _view.addDefinition(navigator().getContext().getString(R.string.no_defintion));
            }
            else{
                for(Definition definition : _definitions)
                    _view.addDefinition(definition._definition);
            }

            _view.setVisibility(View.VISIBLE);
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
        if (_view != null) {
            _view.setVisibility(View.GONE);
            presenter().onQueryTextSubmit(query);
        }
    }
}
