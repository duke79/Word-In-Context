package com.baliyaan.android.wordincontext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Pulkit Singh on 1/10/2018.
 */

public class WordDictActivity
        extends AppCompatActivity {

    private Navigator _navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Set content view*/
        setContentView(R.layout.activity_word_dict);

        _navigator = new Navigator(this);

        //Todo: Replace with #onSearchIntent once SearchBox is also added to layout
        _navigator.onSearchBoxSubmit("frugal");
    }

}
