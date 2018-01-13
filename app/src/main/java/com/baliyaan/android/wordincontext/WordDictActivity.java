package com.baliyaan.android.wordincontext;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.baliyaan.android.wordincontext.Components.Examples.MVP.ViewPort;

/**
 * Created by Pulkit Singh on 1/10/2018.
 */

public class WordDictActivity
        extends AppCompatActivity
        implements com.baliyaan.android.wordincontext.Components.Examples.MVP.Contract.Navigator{

    private com.baliyaan.android.wordincontext.Components.Examples.MVP.Contract.Port _examplesPort = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Set content view*/
        setContentView(R.layout.activity_word_dict);

        _examplesPort = new ViewPort(this);
        _examplesPort.onQueryTextSubmit("frugal");
    }

    @Override
    public String getQuery() {
        return "frugal";
    }

    @Override
    public void onTryAgain() {
        _examplesPort.onQueryTextSubmit("frugal");
    }

    @Override
    public Context getContext() {
        return this;
    }
}
