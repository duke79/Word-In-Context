package com.baliyaan.android.wordincontext.Components.Definition.MVP;

import com.baliyaan.android.mvp.Adapters.MVPPresenterAdapter;
import com.baliyaan.android.wordincontext.Data.DictionaryDB;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
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

        Observable<String> observable = new Observable<String>() {
            @Override
            protected void subscribeActual(Observer observer) {
                String definition = "";/* = OnlineDictionary.getSimpleDefinitionOf(query); // Get definition*/

                DictionaryDB dict = DictionaryDB.GetInstance(view().getContext());
                if (null != dict) {
                    ArrayList<String> matchingWords = dict.GetWordsStartingWith(query, 15);
                    if (matchingWords.size() > 0) {//for (int i = 0; i < matchingWords.size(); i++) {
                        String matchingWord = matchingWords.get(0);
                        ArrayList<String> definitions = dict.GetDefinitionsOf(matchingWord, 15);
                        if (definitions.size() > 0)
                            definition = definitions.get(0);
                    }
                }

                observer.onNext(definition); // Send definition
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
