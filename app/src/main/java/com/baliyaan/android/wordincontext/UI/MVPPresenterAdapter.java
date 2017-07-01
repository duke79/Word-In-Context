package com.baliyaan.android.wordincontext.UI;

import android.app.Activity;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class MVPPresenterAdapter<T>{

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
}
