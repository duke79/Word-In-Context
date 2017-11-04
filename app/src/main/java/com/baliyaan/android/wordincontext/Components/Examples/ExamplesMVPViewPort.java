package com.baliyaan.android.wordincontext.Components.Examples;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.MVPInfra.MVPViewPortAdapter;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class ExamplesMVPViewPort extends MVPViewPortAdapter<ExamplesMVPContract.Navigator, ExamplesMVPContract.Presenter> implements ExamplesMVPContract.View, ExamplesMVPContract.Port {

    private ExamplesView _view;

    public ExamplesMVPViewPort(Activity activity, final ExamplesMVPContract.Navigator navigator) {
        super(activity, navigator);
        super.bindPresenter(new ExamplesMVPPresenter(activity(), this));

        //Configure UIExamplesView
        _view = (ExamplesView) activity().findViewById(R.id.list_view);
        ExamplesPagerAdapter pagerAdapter = new ExamplesPagerAdapter(activity(), presenter().getExamples());
        if (null != _view) {
            _view.setAdapter(pagerAdapter);
        }

        //Configure "Try again" button
        Button button = (Button) _view.findViewById(R.id.try_again);
        if (null != button) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
