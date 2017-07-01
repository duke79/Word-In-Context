package com.baliyaan.android.wordincontext.UI.Examples;

import android.app.Activity;
import android.widget.Toast;

import com.baliyaan.android.wordincontext.Model.WordExample;
import com.baliyaan.android.wordincontext.UI.MVPPresenterAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

import static com.baliyaan.android.wordincontext.Data.Scraper.GetExamples;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

class UIExamplesMVPPresenter extends MVPPresenterAdapter<UIExamplesMVPContract.View> implements UIExamplesMVPContract.Presenter {

    private List<WordExample> _examples = new ArrayList<WordExample>();;

    protected UIExamplesMVPPresenter(Activity activity, UIExamplesMVPContract.View view) {
        super(activity, view);
    }

    @Override
    public Observable onQueryTextSubmit(final String query) {
        Observable<List<WordExample>> observable = new Observable<List<WordExample>>() {
            @Override
            protected void subscribeActual(Observer<? super List<WordExample>> observer) {
                try {
                    final List<WordExample> newList = GetExamples(query);
                    if (newList.size() > 0) {
                            _examples.removeAll(_examples);
                            _examples.addAll(newList);
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
        };

        return observable;
    }

    @Override
    public List<WordExample> getExamples() {
        return _examples;
    }
}
