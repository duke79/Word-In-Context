package com.baliyaan.android.wordincontext.Components.Examples.MVP;


import android.view.View;
import android.widget.Button;

import com.baliyaan.android.mvp.Adapters.MVPViewPortAdapter;
import com.baliyaan.android.wordincontext.Components.Examples.UI.CustomView;
import com.baliyaan.android.wordincontext.Components.Examples.UI.ExamplesPagerAdapter;
import com.baliyaan.android.wordincontext.R;


/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class ViewPort extends MVPViewPortAdapter<Contract.Navigator, Contract.Presenter> implements Contract.View, Contract.Port {

    public ViewPort(final Contract.Navigator navigator, View view) {
        super(navigator, view);
        super.bindPresenter(new Presenter(this));

        //Configure UIExamplesView
        ExamplesPagerAdapter pagerAdapter = new ExamplesPagerAdapter(navigator(), presenter().getExamples());
        if (null != ((CustomView) view())) {
            ((CustomView) view()).setAdapter(pagerAdapter);
        }

        //Configure "Try again" button
        Button button = (Button) ((CustomView) view()).findViewById(R.id.try_again);
        if (null != button) {
            button.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    navigator().onTryAgain();
                }
            });
        }
    }

    @Override
    public void onQueryTextSubmit(final String query) {
        ((CustomView) view()).displayLoading();
        presenter().onQueryTextSubmit(query);
    }


    @Override
    public void displayResult() {
        ((CustomView) view()).displayList();
    }

    @Override
    public void displayError() {
        ((CustomView) view()).displayErrorText();
        //Toast.makeText(activity(), R.string.NoResult, Toast.LENGTH_SHORT).show();
    }
}
