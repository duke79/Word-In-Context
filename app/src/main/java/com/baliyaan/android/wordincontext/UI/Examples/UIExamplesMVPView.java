package com.baliyaan.android.wordincontext.UI.Examples;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.baliyaan.android.wordincontext.Model.WordExample;
import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.UI.MVPViewAdapter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class UIExamplesMVPView extends MVPViewAdapter<UIExamplesMVPContract.Navigator> implements UIExamplesMVPContract.View, UIExamplesMVPContract.Port {

    private ViewPager _viewPager = null;
    private UIExamplesMVPPresenter _presenter = null;
    private ExamplesAdapter _pagerAdapter = null;

    public UIExamplesMVPView(Activity activity, UIExamplesMVPContract.Navigator navigator) {
        super(activity,navigator);
        _presenter = new UIExamplesMVPPresenter(activity(),this);

        _viewPager = (ViewPager) activity().findViewById(R.id.view_pager_examples);

        if (null != _viewPager) {
            _pagerAdapter = new ExamplesAdapter(activity(),_presenter.getExamples());
            _viewPager.setAdapter(_pagerAdapter);
        }
    }

    @Override
    public void onQueryTextSubmit(final String query) {
        _presenter.onQueryTextSubmit(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<WordExample>>() {
            @Override
            public void accept(@NonNull List<WordExample> newList) throws Exception {

                if (_pagerAdapter != null) {
                    activity().findViewById(R.id.progressBar).setVisibility(View.GONE);
                    activity().findViewById(R.id.view_pager_examples).setVisibility(View.VISIBLE);

                    _pagerAdapter.notifyDataSetChanged();
                    _viewPager.setCurrentItem(0);
                }

            }
        });
    }
}
