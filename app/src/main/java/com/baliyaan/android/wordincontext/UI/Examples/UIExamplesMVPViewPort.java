package com.baliyaan.android.wordincontext.UI.Examples;

import android.app.Activity;
import android.widget.Toast;

import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.UI.CustomListView;
import com.baliyaan.android.wordincontext.UI.MVPViewPortAdapter;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class UIExamplesMVPViewPort extends MVPViewPortAdapter<UIExamplesMVPContract.Navigator,UIExamplesMVPContract.Presenter> implements UIExamplesMVPContract.View, UIExamplesMVPContract.Port {

    private CustomListView _view;

    public UIExamplesMVPViewPort(Activity activity, UIExamplesMVPContract.Navigator navigator) {
        super(activity,navigator);
        super.bindPresenter(new UIExamplesMVPPresenter(activity(),this));

        _view = (CustomListView) activity().findViewById(R.id.list_view);
        UIExamplesPagerAdapter pagerAdapter = new UIExamplesPagerAdapter(activity(), presenter().getExamples());
        _view.setAdapter(pagerAdapter);
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
        Toast.makeText(activity(), R.string.NoResult, Toast.LENGTH_SHORT).show();
    }
}