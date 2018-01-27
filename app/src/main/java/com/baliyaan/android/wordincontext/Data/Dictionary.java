package com.baliyaan.android.wordincontext.Data;

import android.content.Context;

import com.baliyaan.android.wordincontext.Components.Examples.Model.Example;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by Pulkit Singh on 6/17/2017.
 */

public class Dictionary {

    private static Dictionary _singleton = null;
    private DictionaryDB _dictDB = null;
    private DatabaseReference _examplesDB = null;

    private Dictionary(final Context context) {
        _dictDB = DictionaryDB.getInstance(context);
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

    public ArrayList<String> getSuggestionsFor(String token, int nbrSuggestions) {
        return _dictDB.getWordsStartingWith(token, nbrSuggestions);
    }

    public Observable<List<Example>> getExamples(final String token) {
        return new Observable<List<Example>>() {
            @Override
            protected void subscribeActual(final Observer<? super List<Example>> observer) {
                //TODO: Temporary code, see below
                /* Temporary block starts*/
                try {
                    observer.onNext(Scraper.GetExamples(token));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /* Temporary block ends*/

                //TODO: Switch to this code once Firebase access is provided
                /*if(null != _examplesDB) {
                    _examplesDB.child(token).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<Example> examples = new ArrayList<>();
                            if(dataSnapshot.exists()) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    Example example = child.getValue(Example.class);
                                    if (null != example) {
                                        examples.add(example);
                                    }
                                }
                            }
                            else{
                                try {
                                    examples.addAll(Scraper.GetExamples(token));
                                    for(Example example:examples) {
                                        dataSnapshot.getRef().push().setValue(example);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            observer.onNext(examples);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(this.getClass().getName(),databaseError.toString());
                        }
                    });
                }*/
            }
        };
    }
}
