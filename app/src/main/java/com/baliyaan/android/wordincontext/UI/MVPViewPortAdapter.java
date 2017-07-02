package com.baliyaan.android.wordincontext.UI;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class MVPViewPortAdapter<N,P extends MVPBasePresenterInterface> implements MVPBaseViewInterface<P>, MVPBasePortInterface{
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

    // MVPBaseViewInterface implementation
    @Override
    public P bindPresenter(P presenter) {
        _presenter = presenter;
        return _presenter;
    }


    // MVPBasePortInterface implementation
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
