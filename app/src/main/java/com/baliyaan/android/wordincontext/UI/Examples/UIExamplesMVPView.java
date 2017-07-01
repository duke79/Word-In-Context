package com.baliyaan.android.wordincontext.UI.Examples;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.baliyaan.android.wordincontext.Model.WordExample;
import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.UI.MVPViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.baliyaan.android.wordincontext.Data.Scraper.GetExamples;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class UIExamplesMVPView extends MVPViewAdapter<UIExamplesMVPContract.Navigator> implements UIExamplesMVPContract.View, UIExamplesMVPContract.Port {

    private List<WordExample> _examples = new ArrayList<WordExample>();;
    private ExamplesAdapter _pagerAdapter = null;
    private ViewPager _viewPager = null;

    public UIExamplesMVPView(Activity activity, UIExamplesMVPContract.Navigator navigator) {
        super(activity,navigator);
        _viewPager = (ViewPager) activity().findViewById(R.id.view_pager_examples);
        _pagerAdapter = new ExamplesAdapter(activity(),_examples);
        if (null != _viewPager) {
            _viewPager.setAdapter(_pagerAdapter);
        }
    }

    @Override
    public void onQueryTextSubmit(final String query) {
        Observable<List<WordExample>> observable = new Observable<List<WordExample>>() {
            @Override
            protected void subscribeActual(Observer<? super List<WordExample>> observer) {
                try {
                    final List<WordExample> newList = GetExamples(query);
                    if (newList.size() > 0) {
                        observer.onNext(newList);
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    (activity()).runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity(), "No internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(   new Consumer<List<WordExample>>() {
            @Override
            public void accept(@NonNull List<WordExample> newList) throws Exception {
                if (_pagerAdapter != null) {
                    activity().findViewById(R.id.progressBar).setVisibility(View.GONE);
                    activity().findViewById(R.id.view_pager_examples).setVisibility(View.VISIBLE);
                    _examples.removeAll(_examples);
                    _examples.addAll(newList);
                    _pagerAdapter.notifyDataSetChanged();
                    _viewPager.setCurrentItem(0);
                }
            }
        });
    }
}
