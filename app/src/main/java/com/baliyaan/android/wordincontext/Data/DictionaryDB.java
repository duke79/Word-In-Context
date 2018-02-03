package com.baliyaan.android.wordincontext.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.baliyaan.android.library.db.SQLiteAssetHelper.SQLiteAssetHelper;
import com.baliyaan.android.library.ds.Trie;
import com.baliyaan.android.library.io.SerializeService;
import com.baliyaan.android.wordincontext.Model.Definition;
import com.baliyaan.android.wordincontext.MyApplication;
import com.baliyaan.android.wordincontext.R;

import java.util.Formatter;
import java.util.HashSet;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Pulkit Singh on 1/21/2018.
 */

public class DictionaryDB extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "Dictionary.db";
    private static final int DATABASE_VERSION = 1;
    private Trie _suggestionsTrie = null;
    private SQLiteDatabase _db = null;

    DictionaryDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade(); //ToDo: Comment it out to enable #onUpgrade() callback on version increment

        /*Initialize suggestions Trie*/
        deserializeSuggestionsTrie()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Trie>() {
                    @Override
                    public void accept(Trie trie) throws Exception {
                    }
                });

        /*if (null == _suggestionsTrie)
            serializeSuggestionsTrie();*/

        /*Initi words hash*/
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        //serializeSuggestionsTrie();
        /*CREATE INDEX tag_word from entries(word);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //serializeSuggestionsTrie();

        _db = db;
        /*switch (newVersion) {
            case 1:
                ;
            case 2:
                ;
            case 3:
                ;
            case 4:
                ;
            case 5:
                ;
            case 6:
                Cursor cursor = sqlQuery("CREATE INDEX tag_word on entries(word)");
        }*/
        _db = null; // To be unset after upgrade, because it gets closed and stays no more usable (https://stackoverflow.com/a/31508029/973425)
        //super.onUpgrade(db, oldVersion, newVersion);
    }

    /*Public API*/

    /**
     * Get the suggestions for a prefix
     *
     * @param input
     * @param n
     * @return Observable to be subscribed on
     */
    Observable<HashSet<String>> getSuggestions(final String input, final int n) {
        return new Observable<HashSet<String>>() {
            @Override
            protected void subscribeActual(Observer<? super HashSet<String>> observer) {
                try {
                    StringBuilder sb = new StringBuilder();
                    Formatter formatter = new Formatter(sb, Locale.US);
                    String query = formatter.format(MyApplication.getAppContext().getString(R.string.Dicitionary_db__MatchingWords), input, n).toString();

                    if (null != _suggestionsTrie) {
                        HashSet<String> suggestions = new HashSet<>();
                        suggestions.addAll(_suggestionsTrie.getWordsStartingWith(input, n));
                        observer.onNext(suggestions);
                    } else {
                        Cursor cursor = sqlQuery(query);
                        if (null != cursor) {
                            HashSet<String> suggestions = new HashSet<>();
                            int i = 0;
                            if (cursor.moveToFirst()) {
                                do {
                                    String sug = cursor.getString(0);
                                    suggestions.add(sug);
                                } while (cursor.moveToNext() && (i++ < n));
                            }
                            cursor.close();
                            observer.onNext(suggestions);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Definitions of a word.
     * Note : Definitions may be of different senses. This method doesn't offer a distinction.
     *
     * @param input
     * @param n
     * @return Observable to be subscribed on
     */
    Observable<Definition> getDefinitions(final String input, final int n) {

        Observable<Definition> observable =
                new Observable<Definition>() {
                    @Override
                    protected void subscribeActual(Observer observer) {
                        //String definition = "";/* = OnlineDictionary.getSimpleDefinitionOf(query); // Get definition*/
                        StringBuilder sb = new StringBuilder();
                        Formatter formatter = new Formatter(sb, Locale.US);
                        String query = formatter.format(MyApplication.getAppContext().getString(R.string.Dicitionary_db__WordDefinitions), input, n).toString();

                        Cursor cursor = sqlQuery(query);

                        //Traverse cursor
                        if (null != cursor) {
                            if (cursor.moveToFirst()) {
                                do {
                                    try {
                                        String[] strDefinitions = cursor.getString(2).split(";");
                                        for (String strDefinition : strDefinitions) {
                                            Definition definition = new Definition();
                                            //definition._id = cursor.getString(0);
                                            definition._headword = cursor.getString(0);
                                            definition._partOfSpeech = cursor.getString(1);
                                            definition._definition = strDefinition;
                                            //Log.i("SQLiteAssetHelperTest",word);
                                            observer.onNext(definition); // Send definition
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } while (cursor.moveToNext());
                            }
                            cursor.close();
                        }
                    }
                };

        return observable;
    }

    /**
     * Get all the words in the dictionary database.
     *
     * @return
     */
    Observable<String> getAll() {
        return new Observable<String>() {
            @Override
            protected void subscribeActual(Observer<? super String> observer) {

                Cursor cursor = sqlQuery("select word from entries");

                /*Traverse cursor & retrieve results*/
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        do {
                            try {
                                String strWord = cursor.getString(0);
                                observer.onNext(strWord);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    observer.onComplete();
                }
            }
        };
    }

    /*Private methods*/
    private void serializeSuggestionsTrie() {
        _suggestionsTrie = new Trie();
        /*Create Trie of words from Dictionary.db*/
        getAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                        String strWord = s.toLowerCase();
                        _suggestionsTrie.add(strWord);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        SerializeService.serialize(_suggestionsTrie, MyApplication.getAppContext(), MyApplication.getAppContext().getString(R.string.FileSuggestionsTrie));
                    }
                });
    }

    private Observable<Trie> deserializeSuggestionsTrie() {
        return new Observable<Trie>() {
            @Override
            protected void subscribeActual(Observer<? super Trie> observer) {
                _suggestionsTrie = (Trie) SerializeService.deserialize(MyApplication.getAppContext(), MyApplication.getAppContext().getString(R.string.FileSuggestionsTrie));
                if (null == _suggestionsTrie)
                    serializeSuggestionsTrie();
            }
        };
    }

    private Cursor sqlQuery(String query) {
        Cursor cursor = null;
        try {
            if (null == _db)
                _db = getWritableDatabase();
            cursor = _db.rawQuery(query, null);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.toString());
        }
        return cursor;
    }
}
