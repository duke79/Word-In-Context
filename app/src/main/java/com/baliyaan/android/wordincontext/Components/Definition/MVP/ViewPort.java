package com.baliyaan.android.wordincontext.Components.Definition.MVP;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baliyaan.android.mvp.Adapters.MVPViewPortAdapter;
import com.baliyaan.android.wordincontext.Components.Definition.UI.CustomAdapter;
import com.baliyaan.android.wordincontext.Model.Definition;

import java.util.ArrayList;

/**
 * Created by Pulkit Singh on 11/5/2017.
 */

public class ViewPort
        extends MVPViewPortAdapter<Contract.Navigator, Contract.Presenter>
        implements Contract.View, Contract.Port {
    private ArrayList<Definition> _definitions = new ArrayList<>();
    private CustomAdapter _adapter;

    public ViewPort(Contract.Navigator navigator, View view) {
        super(navigator, view);
        super.bindPresenter(new Presenter(this));
        init();
    }

    private void init() {
        /*Dummy Definition*/
        Definition definition = new Definition();
        definition._definition = "Hello hello, I'm kinda busy!";
        _definitions.add(definition);
        /*Dummy Definition*/

        _adapter = new CustomAdapter(_definitions);
        _adapter.setHasStableIds(true);
        ((RecyclerView)view()).setAdapter(_adapter);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        //lm.setOrientation(VERTICAL);
        ((RecyclerView)view()).setLayoutManager(lm);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        ((RecyclerView)view()).setItemAnimator(animator);
    }

    @Override
    public void addDefinition(Definition definition) {
        _definitions.add(definition);
        _adapter.notifyItemInserted(_definitions.size()-1);
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
        if(null != definitions) {
            for (Definition definition : definitions)
                addDefinition(definition);
        }
    }

    @Override
    public void onQueryTextSubmit(String query) {
        if (view() != null) {
            presenter().onQueryTextSubmit(query);
        }
    }
}
