package com.baliyaan.android.wordincontext.UI.Examples;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.baliyaan.android.wordincontext.Model.WordExample;
import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.UI.MVPViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<WordExample> newList = GetExamples(query);
                    if (newList.size() > 0) {
                        Handler handler = new Handler(activity().getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
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
                } catch (IOException e) {
                    e.printStackTrace();

                    (activity()).runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity(), "No internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
