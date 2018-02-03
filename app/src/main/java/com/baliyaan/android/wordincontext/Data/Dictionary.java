package com.baliyaan.android.wordincontext.Data;

import android.content.Context;
import android.util.Log;

import com.baliyaan.android.wordincontext.Components.Examples.Model.Example;
import com.baliyaan.android.wordincontext.Model.Definition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Pulkit Singh on 6/17/2017.
 */

public class Dictionary {

    private static Dictionary _singleton = null;
    private DictionaryDB _dictDB = null;
    private DatabaseReference _examplesDB = null;

    private Dictionary(final Context context) {
        _dictDB = new DictionaryDB(context);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (null != database) {
            _examplesDB = database.getReference("word_in_context");
            _examplesDB.keepSynced(true);
        }
    }

    public static Dictionary getInstance(Context context) {
        if (null == _singleton)
            _singleton = new Dictionary(context);
        return _singleton;
    }

    public Observable<HashSet<String>> getSuggestionsFor(final String token, final int nbrSuggestions) {
        return _dictDB.getSuggestions(token, nbrSuggestions);
    }

    public Observable<Definition> getDefinitionsFor(String input, int n) {
        return _dictDB.getDefinitions(input, n);
    }

    public Observable<List<Example>> getExamplesFor(final String token) {
        return new Observable<List<Example>>() {
            @Override
            protected void subscribeActual(final Observer<? super List<Example>> observer) {
                if (null != _examplesDB) {
                    _examplesDB.child(token).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                ArrayList<Example> examples = new ArrayList<>();
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    Example example = child.getValue(Example.class);
                                    if (null != example) {
                                        examples.add(example);
                                    }
                                }
                                observer.onNext(examples);
                            } else {
                                Scraper.GetExamples(token)
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(Schedulers.newThread())
                                        .subscribe(new Consumer<List<Example>>() {
                                            @Override
                                            public void accept(List<Example> examples) throws Exception {
                                                observer.onNext(examples);
                                                for (Example example : examples) {
                                                    dataSnapshot.getRef().push().setValue(example);
                                                }
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(this.getClass().getName(), databaseError.toString());
                        }
                    });
                }
            }
        };
    }
}
