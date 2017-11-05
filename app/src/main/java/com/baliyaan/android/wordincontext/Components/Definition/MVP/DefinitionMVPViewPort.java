package com.baliyaan.android.wordincontext.Components.Definition.MVP;

import android.app.Activity;

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
    }

    @Override
    public void setDefinition(String definition) {
        _view.setDefinition(definition);
    }

    @Override
    public void onQueryTextSubmit(String query) {
        presenter().onQueryTextSubmit(query);
    }
}
