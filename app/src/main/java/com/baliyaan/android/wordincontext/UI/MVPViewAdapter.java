package com.baliyaan.android.wordincontext.UI;

import android.app.Activity;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class MVPViewAdapter<T> {
    private T _navigator;
    private Activity _activity = null;

    protected MVPViewAdapter(Activity activity, T navigator){
        _activity = activity;
        _navigator = navigator;
    }

    protected T navigator(){
        return _navigator;
    }

    protected Activity activity(){
        return _activity;
    }
}
