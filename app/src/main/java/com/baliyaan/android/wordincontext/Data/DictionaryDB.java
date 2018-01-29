package com.baliyaan.android.wordincontext.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.baliyaan.android.library.db.SQLiteAssetHelper.SQLiteAssetHelper;
import com.baliyaan.android.library.ds.Trie;
import com.baliyaan.android.library.io.SerializeService;
import com.baliyaan.android.wordincontext.Model.Definition;
import com.baliyaan.android.wordincontext.R;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Pulkit Singh on 1/21/2018.
 */

public class DictionaryDB extends SQLiteAssetHelper {
    private static DictionaryDB _dict = null;
    private static final String DATABASE_NAME = "Dictionary.db";
    private static final int DATABASE_VERSION = 1;
    private Trie _suggestionsTrie = null;
    private SQLiteDatabase _db = null;
    private Context _context = null;

    private DictionaryDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _context = context;
        setForcedUpgrade();

        /*Initialize suggestions Trie*/
        deserializeSuggestionsTrie()
                .subscribeOn(Schedulers.newThread());
                /*.observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Trie>() {
                    @Override
                    public void accept(Trie trie) throws Exception {

                    }
                });*/

        if (null == _suggestionsTrie)
            serializeSuggestionsTrie();
    }

    public static DictionaryDB getInstance(Context context) {
        if (null == _dict)
            _dict = new DictionaryDB(context);
        return _dict;
    }

    public ArrayList<String> getWordsStartingWith(String input, int n) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        String query = formatter.format(_context.getString(R.string.Dicitionary_db__MatchingWords), input, n).toString();

        Cursor cursor = null;
        try {
            if (null == _db)
                _db = getWritableDatabase();
            cursor = _db.rawQuery(query, null);
        } catch (Exception e) {
            Log.e("DictionaryDB", e.toString());
        }
        //Traverse cursor
        HashSet<String> hash = new HashSet<>();
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    String word = cursor.getString(0);
                    word = word.toLowerCase();
                    //Log.i("SQLiteAssetHelperTest",word);
                    hash.add(word);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        ArrayList list = new ArrayList();
        list.addAll(hash);

        return list;
    }

    public Observable<ArrayList<String>> getObservableWordsStartingWith(final String input, final int n) {
        return new Observable<ArrayList<String>>() {
            @Override
            protected void subscribeActual(Observer<? super ArrayList<String>> observer) {
                if(null != _suggestionsTrie) {
                    ArrayList<String> suggestions = _suggestionsTrie.getWordsStartingWith(input, n);
                    if(null == suggestions)
                        suggestions = new ArrayList<>();
                    observer.onNext(suggestions);
                }
            }
        };
    }

    public Observable<Definition> getObservableDefinitionsOf(final String input, final int n) {

        Observable<Definition> observable =
                new Observable<Definition>() {
                    @Override
                    protected void subscribeActual(Observer observer) {
                        //String definition = "";/* = OnlineDictionary.getSimpleDefinitionOf(query); // Get definition*/
                        StringBuilder sb = new StringBuilder();
                        Formatter formatter = new Formatter(sb, Locale.US);
                        String query = formatter.format(_context.getString(R.string.Dicitionary_db__WordDefinitions), input, n).toString();

                        Cursor cursor = null;
                        try {
                            if (null == _db)
                                _db = getWritableDatabase();
                            cursor = _db.rawQuery(query, null);
                        } catch (Exception e) {
                            Log.e("DictionaryDB", e.toString());
                        }
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

    public Observable<String> getAllWords() {
        return new Observable<String>() {
            @Override
            protected void subscribeActual(Observer<? super String> observer) {
                /*Build query string*/
                StringBuilder sb = new StringBuilder();
                Formatter formatter = new Formatter(sb, Locale.US);
                String query = formatter.format("select word from entries").toString();

                /*Execute query & retrieve cursor*/
                Cursor cursor = null;
                try {
                    if (null == _db)
                        _db = getWritableDatabase();
                    cursor = _db.rawQuery(query, null);
                } catch (Exception e) {
                    Log.e("DictionaryDB", e.toString());
                }

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


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    //ToDo: Create index and test with suggestions
    //ToDo: Update AndroidLibarary sQLite to support onCreate
    //ToDo: Handle onUpgrade like a pro by revisions(case 1:sql ; no break case 2:more sql ) & onCreate (to keep everything for the new user in this function
    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        serializeSuggestionsTrie();
        /*CREATE INDEX tag_word from entries(word);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        serializeSuggestionsTrie();
        /*CREATE INDEX tag_word from entries(word);*/
    }

    private void serializeSuggestionsTrie() {
        _suggestionsTrie = new Trie();
        /*Create Trie of words from Dictionary.db*/
        getAllWords()
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
                        SerializeService.serialize(_suggestionsTrie, _context, _context.getString(R.string.FileSuggestionsTrie));
                    }
                });
    }

    public Observable<Trie> deserializeSuggestionsTrie(){
        return new Observable<Trie>() {
            @Override
            protected void subscribeActual(Observer<? super Trie> observer) {
                _suggestionsTrie = (Trie) SerializeService.deserialize(_context, _context.getString(R.string.FileSuggestionsTrie));
                if(null == _suggestionsTrie)
                    serializeSuggestionsTrie();
            }
        };
    }
}
