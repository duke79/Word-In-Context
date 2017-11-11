package com.baliyaan.android.wordincontext.Components.Examples.MVP;


import android.app.Activity;
import android.widget.Button;

import com.baliyaan.android.mvp.Adapters.MVPViewPortAdapter;
import com.baliyaan.android.wordincontext.Components.Examples.UI.CustomView;
import com.baliyaan.android.wordincontext.Components.Examples.UI.ExamplesPagerAdapter;
import com.baliyaan.android.wordincontext.R;


/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class ViewPort extends MVPViewPortAdapter<Contract.Navigator, Contract.Presenter> implements Contract.View, Contract.Port {

    private CustomView _view;

    public ViewPort(final Contract.Navigator navigator) {
        super(navigator);
        super.bindPresenter(new Presenter(this));

        //Configure UIExamplesView
        _view = (CustomView) ((Activity) navigator().getContext()).findViewById(R.id.list_view);
        ExamplesPagerAdapter pagerAdapter = new ExamplesPagerAdapter(navigator(), presenter().getExamples());
        if (null != _view) {
            _view.setAdapter(pagerAdapter);
        }

        //Configure "Try again" button
        Button button = (Button) _view.findViewById(R.id.try_again);
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
        _view.displayLoading();
        presenter().onQueryTextSubmit(query);
    }


    @Override
    public void displayResult() {
        _view.displayList();
    }

    @Override
    public void displayError() {
        _view.displayErrorText();
        //Toast.makeText(activity(), R.string.NoResult, Toast.LENGTH_SHORT).show();
    }
}
