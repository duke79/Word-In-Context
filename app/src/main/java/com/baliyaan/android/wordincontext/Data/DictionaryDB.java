package com.baliyaan.android.wordincontext.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.baliyaan.android.library.db.SQLiteAssetHelper.SQLiteAssetHelper;
import com.baliyaan.android.wordincontext.Model.Definition;
import com.baliyaan.android.wordincontext.R;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by Pulkit Singh on 1/21/2018.
 */

public class DictionaryDB extends SQLiteAssetHelper {
    private static DictionaryDB _dict = null;
    private static final String DATABASE_NAME = "Dictionary.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase _db = null;
    private Context _context = null;

    private DictionaryDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _context = context;
        setForcedUpgrade();
    }

    public static DictionaryDB getInstance(Context context)
    {
        if(null == _dict)
            _dict = new DictionaryDB(context);
        return _dict;
    }

    public ArrayList<String> getWordsStartingWith(String input, int n) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        String query = formatter.format(_context.getString(R.string.Dicitionary_db__MatchingWords),input,n).toString();

        Cursor cursor = null;
        try {
            if(null == _db)
                _db = getWritableDatabase();
            cursor = _db.rawQuery(query,null);
        }
        catch (Exception e){
            Log.e("DictionaryDB",e.toString());
        }
        //Traverse cursor
        HashSet<String> hash = new HashSet<>();
        if(null != cursor) {
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

    public ArrayList<Definition> getDefinitionsOf(String input, int n){
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        String query = formatter.format(_context.getString(R.string.Dicitionary_db__WordDefinitions),input,n).toString();

        Cursor cursor = null;
        try {
            if(null == _db)
                _db = getWritableDatabase();
            cursor = _db.rawQuery(query,null);
        }
        catch (Exception e){
            Log.e("DictionaryDB",e.toString());
        }
        //Traverse cursor
        ArrayList<Definition> list = new ArrayList<>();
        if(null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        String[] strDefinitions = cursor.getString(2).split(";");
                        for(String strDefinition : strDefinitions)
                        {
                            Definition definition = new Definition();
                            //definition._id = cursor.getString(0);
                            definition._headword = cursor.getString(0);
                            definition._partOfSpeech = cursor.getString(1);
                            definition._definition = strDefinition;
                            //Log.i("SQLiteAssetHelperTest",word);
                            list.add(definition);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    public Observable<ArrayList<Definition>> getObservableDefinitionsOf(final String input, int n){

        Observable<ArrayList<Definition>> observable =
                new Observable<ArrayList<Definition>>() {
            @Override
            protected void subscribeActual(Observer observer) {
                //String definition = "";/* = OnlineDictionary.getSimpleDefinitionOf(query); // Get definition*/
                ArrayList<Definition> definitions = null;
                definitions = getDefinitionsOf(input, 15);
                observer.onNext(definitions); // Send definition
            }
        };

        return observable;
    }
}
