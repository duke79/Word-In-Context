package com.baliyaan.android.wordincontext.Components.Examples.MVP;

import android.app.Activity;
import android.widget.Toast;

import com.baliyaan.android.wordincontext.Components.Examples.Model.Example;
import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.MVPInfra.Adapters.MVPPresenterAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.baliyaan.android.wordincontext.Components.Examples.Data.Scraper.GetExamples;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

class ExamplesMVPPresenter extends MVPPresenterAdapter<ExamplesMVPContract.MVPView> implements ExamplesMVPContract.MVPPresenter {

    private List<Example> _examples = new ArrayList<Example>();;

    protected ExamplesMVPPresenter(Activity activity, ExamplesMVPContract.MVPView view) {
        super(activity, view);
    }

    @Override
    public void onQueryTextSubmit(final String query) {
        Observable<List<Example>> observable = new Observable<List<Example>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Example>> observer) {
                try {
                    final List<Example> newList = GetExamples(query);
                    if (newList.size() > 0) {
                            _examples.removeAll(_examples);
                            _examples.addAll(newList);
                    }
                    observer.onNext(newList);
                } catch (IOException e) {
                    e.printStackTrace();
                    final List<Example> newList = new ArrayList<>();
                    observer.onNext(newList);

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
                .subscribe(new Consumer<List<Example>>() {
                    @Override
                    public void accept(@NonNull List<Example> newList) throws Exception {
                        if(newList.size()>0)
                            view().displayResult();
                        else
                            view().displayError();
                    }
                });
    }

    @Override
    public List<Example> getExamples() {
        return _examples;
    }
}
