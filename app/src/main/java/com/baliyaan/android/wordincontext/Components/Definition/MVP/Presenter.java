package com.baliyaan.android.wordincontext.Components.Definition.MVP;

import com.baliyaan.android.mvp.Adapters.MVPPresenterAdapter;
import com.baliyaan.android.wordincontext.Data.DictionaryDB;
import com.baliyaan.android.wordincontext.Model.Definition;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Pulkit Singh on 11/5/2017.
 */

public class Presenter
        extends MVPPresenterAdapter<Contract.View>
        implements Contract.Presenter {
    protected Presenter(Contract.View view) {
        super(view);
    }

    @Override
    public void onQueryTextSubmit(final String query) {

        DictionaryDB dict = DictionaryDB.getInstance(view().getContext());
        dict.getObservableDefinitionsOf(query, 15)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Definition>>() {
                    @Override
                    public void accept(@NonNull ArrayList<Definition> definitions) throws Exception {
                        view().setDefinitions(definitions);
                    }
                });

    }
}
