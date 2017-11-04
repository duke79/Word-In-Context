package com.baliyaan.android.wordincontext.MVPInfra.Adapters;

import android.app.Activity;
import android.os.Bundle;

import com.baliyaan.android.wordincontext.MVPInfra.Interfaces.BaseMVPPort;
import com.baliyaan.android.wordincontext.MVPInfra.Interfaces.BaseMVPPresenter;
import com.baliyaan.android.wordincontext.MVPInfra.Interfaces.BaseMVPView;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class MVPViewPortAdapter<N,P extends BaseMVPPresenter> implements BaseMVPView<P>, BaseMVPPort {
    private N _navigator;
    private Activity _activity = null;
    protected P _presenter;

    protected MVPViewPortAdapter(Activity activity, N navigator){
        _activity = activity;
        _navigator = navigator;
    }

    protected N navigator(){
        return _navigator;
    }

    protected Activity activity(){
        return _activity;
    }

    protected P presenter() {
        return _presenter;
    }

    // BaseMVPView implementation
    @Override
    public P bindPresenter(P presenter) {
        _presenter = presenter;
        return _presenter;
    }


    // BaseMVPPort implementation
    @Override
    public void onSaveState() {
        if(null != presenter())
            presenter().onSaveState();
    }

    @Override
    public void onResumeState() {
        if(null != presenter())
            presenter().onResumeState();
    }

    @Override
    public Bundle getState() {
        return presenter().getState();
    }

    @Override
    public void setState(Bundle state) {
        presenter().setState(state);
    }
}
