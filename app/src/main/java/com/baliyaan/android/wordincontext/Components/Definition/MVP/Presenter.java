package com.baliyaan.android.wordincontext.Components.Definition.MVP;

import com.baliyaan.android.mvp.Adapters.MVPPresenterAdapter;
import com.baliyaan.android.wordincontext.Data.Dictionary;
import com.baliyaan.android.wordincontext.Model.Definition;

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

        Dictionary.getInstance(view().getContext()).getDefinitionsFor(query, 15)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Definition>() {
                    @Override
                    public void accept(@NonNull Definition definition) throws Exception {
                        view().addDefinition(definition);
                    }
                });

    }
}
