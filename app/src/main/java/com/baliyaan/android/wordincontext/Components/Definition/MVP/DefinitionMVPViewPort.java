package com.baliyaan.android.wordincontext.Components.Definition.MVP;

import android.app.Activity;
import android.view.View;

import com.baliyaan.android.mvp.Adapters.MVPViewPortAdapter;
import com.baliyaan.android.wordincontext.Components.Definition.UI.DefinitionView;
import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 11/5/2017.
 */

public class DefinitionMVPViewPort
        extends MVPViewPortAdapter<DefinitionMVPContract.Navigator, DefinitionMVPContract.Presenter>
        implements DefinitionMVPContract.View, DefinitionMVPContract.Port {
    private DefinitionView _view;

    public DefinitionMVPViewPort(DefinitionMVPContract.Navigator navigator) {
        super(navigator);
        super.bindPresenter(new DefinitionMVPPresenter(this));
        init();
    }

    private void init() {
        _view = (DefinitionView) ((Activity)navigator().getContext()).findViewById(R.id.definition_view);
        _view.setVisibility(View.GONE);
    }

    @Override
    public void setDefinition(String definition) {
        if(definition == "")
            definition = navigator().getContext().getString(R.string.no_defintion);
        _view.setDefinition(definition);
        _view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onQueryTextSubmit(String query) {
        _view.setVisibility(View.GONE);
        presenter().onQueryTextSubmit(query);
    }
}
