package com.baliyaan.android.wordincontext.Components.Definition.MVP;

import android.app.Activity;
import android.widget.Toast;

import com.baliyaan.android.mvp.Adapters.MVPPresenterAdapter;
import com.baliyaan.android.wordincontext.Components.Definition.Data.OnlineDictionary;
import com.baliyaan.android.wordincontext.R;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Pulkit Singh on 11/5/2017.
 */

public class DefinitionMVPPresenter
        extends MVPPresenterAdapter<DefinitionMVPContract.View>
implements DefinitionMVPContract.Presenter{
    protected DefinitionMVPPresenter(DefinitionMVPContract.View view) {
        super(view);
    }

    @Override
    public void onQueryTextSubmit(final String query) {

        Observable<String> observable = new Observable<String>() {
            @Override
            protected void subscribeActual(Observer observer) {
                try {
                    String definition = OnlineDictionary.getDefinitionOf(query); // Get definition
                    observer.onNext(definition); // Send definition
                } catch (IOException e) {
                    e.printStackTrace();
                    observer.onNext(""); // "" (empty) definition

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
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String definition) throws Exception {
                        view().setDefinition(definition);
                    }
                });
    }
}
