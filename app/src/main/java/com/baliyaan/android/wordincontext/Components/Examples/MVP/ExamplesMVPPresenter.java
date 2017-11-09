package com.baliyaan.android.wordincontext.Components.Examples.MVP;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.baliyaan.android.mvp.Adapters.MVPPresenterAdapter;
import com.baliyaan.android.wordincontext.Components.Examples.Model.Example;
import com.baliyaan.android.wordincontext.R;

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

class ExamplesMVPPresenter extends MVPPresenterAdapter<ExamplesMVPContract.View> implements ExamplesMVPContract.Presenter {

    private List<Example> _examples = new ArrayList<Example>();;

    protected ExamplesMVPPresenter(ExamplesMVPContract.View view) {
        super(view);
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

                    ((Activity)view().getContext()).runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(view().getContext(), R.string.NoInternet, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onSaveState(Bundle state) {
        super.onSaveState(state);
        state.putParcelableArrayList("examples", (ArrayList<? extends Parcelable>) _examples);
    }

    @Override
    public void onRestoreState(Bundle state) {
        super.onRestoreState(state);
        List<Example> savedList = state.getParcelableArrayList("examples");
        _examples.removeAll(savedList);
        _examples.addAll(savedList);
        view().displayResult();
    }
}
