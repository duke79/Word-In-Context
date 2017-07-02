package com.baliyaan.android.wordincontext.UI;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class MVPPresenterAdapter<T> implements MVPBasePresenterInterface{

    private T _view = null;
    private Activity _activity = null;

    protected MVPPresenterAdapter(Activity activity, T view){
        _activity = activity;
        _view = view;
    }

    protected T view(){
        return _view;
    }

    protected Activity activity(){
        return _activity;
    }

    // MVPBasePresenterInterface implementation
    Bundle _state = null;

    @Override
    public void onSaveState() {
        //Bundle bundle = null;
        //setState(bundle);
    }

    @Override
    public void onResumeState() {
        //Bundle bundle = getState();
        //bundle.getString("key1");
    }

    @Override
    public Bundle getState() {
        return _state;
    }

    @Override
    public void setState(Bundle state) {
        _state = state;
    }
}
