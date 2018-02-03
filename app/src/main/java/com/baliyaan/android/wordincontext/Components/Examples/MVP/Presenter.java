package com.baliyaan.android.wordincontext.Components.Examples.MVP;

import android.os.Bundle;
import android.os.Parcelable;

import com.baliyaan.android.mvp.Adapters.MVPPresenterAdapter;
import com.baliyaan.android.wordincontext.Components.Examples.Model.Example;
import com.baliyaan.android.wordincontext.Data.Dictionary;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Pulkit Singh on 7/1/2017.
 */

class Presenter extends MVPPresenterAdapter<Contract.View> implements Contract.Presenter {

    private List<Example> _examples = new ArrayList<Example>();
    private Dictionary _dictionary = null;

    protected Presenter(Contract.View view) {
        super(view);
        _dictionary = Dictionary.getInstance(view().getContext());
    }

    @Override
    public void onQueryTextSubmit(final String query) {
        if (null != _dictionary) {
            _dictionary.getExamplesFor(query)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Example>>() {
                        @Override
                        public void accept(@NonNull List<Example> newList) throws Exception {
                            if (newList.size() > 0) {
                                _examples.clear();
                                _examples.addAll(newList);
                                view().displayResult();
                            } else
                                view().displayError();
                        }
                    });
        }
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
        _examples.clear();
        _examples.addAll(savedList);
        view().displayResult();
    }
}
