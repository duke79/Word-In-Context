package com.baliyaan.android.wordincontext.UI.Examples;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.UI.MVPViewPortAdapter;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class UIExamplesMVPViewPort extends MVPViewPortAdapter<UIExamplesMVPContract.Navigator,UIExamplesMVPContract.Presenter> implements UIExamplesMVPContract.View, UIExamplesMVPContract.Port {

    private ViewPager _viewPager = null;
    private UIExamplesPagerAdapter _pagerAdapter = null;

    public UIExamplesMVPViewPort(Activity activity, UIExamplesMVPContract.Navigator navigator) {
        super(activity,navigator);
        presenter(new UIExamplesMVPPresenter(activity(),this));

        _viewPager = (ViewPager) activity().findViewById(R.id.view_pager_examples);

        if (null != _viewPager) {
            _pagerAdapter = new UIExamplesPagerAdapter(activity(),presenter().getExamples());
            _viewPager.setAdapter(_pagerAdapter);
        }
    }

    @Override
    public void onQueryTextSubmit(final String query) {
        presenter().onQueryTextSubmit(query);
    }


    @Override
    public void displayResult() {
        if (_pagerAdapter != null) {
            activity().findViewById(R.id.progressBar).setVisibility(View.GONE);
            activity().findViewById(R.id.view_pager_examples).setVisibility(View.VISIBLE);

            _pagerAdapter.notifyDataSetChanged();
            _viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void displayError() {
        activity().findViewById(R.id.progressBar).setVisibility(View.GONE);
        activity().findViewById(R.id.welcomeText).setVisibility(View.VISIBLE);
        Toast.makeText(activity(), R.string.NoResult, Toast.LENGTH_SHORT).show();
    }
}
