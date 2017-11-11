package com.baliyaan.android.wordincontext.Components.Definition.MVP;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.baliyaan.android.mvp.Adapters.MVPViewPortAdapter;
import com.baliyaan.android.wordincontext.Components.Definition.UI.CustomView;
import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 11/5/2017.
 */

public class ViewPort
        extends MVPViewPortAdapter<Contract.Navigator, Contract.Presenter>
        implements Contract.View, Contract.Port {
    private CustomView _view;
    private String _definition;

    public ViewPort(Contract.Navigator navigator) {
        super(navigator);
        super.bindPresenter(new Presenter(this));
        init();
    }

    private void init() {
        _view = (CustomView) ((Activity)navigator().getContext()).findViewById(R.id.definition_view);
        _view.setVisibility(View.GONE);
    }

    @Override
    public void setDefinition(String definition) {
        _definition = definition;
        if(_definition == "")
            _definition = navigator().getContext().getString(R.string.no_defintion);
        _view.setDefinition(_definition);
        _view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveState(Bundle state) {
        super.onSaveState(state);
        state.putString("definition",_definition);
    }

    @Override
    public void onRestoreState(Bundle state) {
        super.onRestoreState(state);
        String definition = state.getString("definition");
        setDefinition(definition);
    }

    @Override
    public void onQueryTextSubmit(String query) {
        _view.setVisibility(View.GONE);
        presenter().onQueryTextSubmit(query);
    }
}
