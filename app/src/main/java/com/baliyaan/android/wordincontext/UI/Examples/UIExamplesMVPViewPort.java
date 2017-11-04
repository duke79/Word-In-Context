package com.baliyaan.android.wordincontext.UI.Examples;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.UI.MVPViewPortAdapter;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class UIExamplesMVPViewPort extends MVPViewPortAdapter<UIExamplesMVPContract.Navigator, UIExamplesMVPContract.Presenter> implements UIExamplesMVPContract.View, UIExamplesMVPContract.Port {

    private UIExamplesView _view;

    public UIExamplesMVPViewPort(Activity activity, final UIExamplesMVPContract.Navigator navigator) {
        super(activity, navigator);
        super.bindPresenter(new UIExamplesMVPPresenter(activity(), this));

        //Configure UIExamplesView
        _view = (UIExamplesView) activity().findViewById(R.id.list_view);
        UIExamplesPagerAdapter pagerAdapter = new UIExamplesPagerAdapter(activity(), presenter().getExamples());
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
