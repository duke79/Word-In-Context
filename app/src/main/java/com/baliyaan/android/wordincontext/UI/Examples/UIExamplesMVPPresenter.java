package com.baliyaan.android.wordincontext.UI.Examples;

import android.app.Activity;
import android.widget.Toast;

import com.baliyaan.android.wordincontext.Model.WordExample;
import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.UI.MVPPresenterAdapter;

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

class UIExamplesMVPPresenter extends MVPPresenterAdapter<UIExamplesMVPContract.View> implements UIExamplesMVPContract.Presenter {

    private List<WordExample> _examples = new ArrayList<WordExample>();;

    protected UIExamplesMVPPresenter(Activity activity, UIExamplesMVPContract.View view) {
        super(activity, view);
    }

    @Override
    public void onQueryTextSubmit(final String query) {
        Observable<List<WordExample>> observable = new Observable<List<WordExample>>() {
            @Override
            protected void subscribeActual(Observer<? super List<WordExample>> observer) {
                try {
                    final List<WordExample> newList = GetExamples(query);
                    if (newList.size() > 0) {
                            _examples.removeAll(_examples);
                            _examples.addAll(newList);
                    }
                    observer.onNext(newList);
                } catch (IOException e) {
                    e.printStackTrace();

                    (activity()).runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity(), R.string.NoInternet, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<WordExample>>() {
                    @Override
                    public void accept(@NonNull List<WordExample> newList) throws Exception {
                        if(newList.size()>0)
                            view().displayResult();
                        else
                            view().displayError();
                    }
                });
    }

    @Override
    public List<WordExample> getExamples() {
        return _examples;
    }
}
