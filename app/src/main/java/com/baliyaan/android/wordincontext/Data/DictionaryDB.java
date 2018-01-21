package com.baliyaan.android.wordincontext.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.baliyaan.android.library.db.SQLiteAssetHelper.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Locale;

/**
 * Created by Pulkit Singh on 1/21/2018.
 */

public class DictionaryDB extends SQLiteAssetHelper {
    private static DictionaryDB _dict = null;
    private static final String DATABASE_NAME = "Dictionary.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase _db = null;

    private DictionaryDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    public static DictionaryDB GetInstance(Context context)
    {
        if(null == _dict)
            _dict = new DictionaryDB(context);
        return _dict;
    }

    public ArrayList<String> GetWordsStartingWith(String input, int n) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        String query = formatter.format("select word from entries where word like \"%1$1s%%\" limit %2$2s;",input,n).toString();

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

    public ArrayList<String> GetDefinitionsOf(String input, int n) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        String query = formatter.format("select definition from entries where word=\"%1$1s\" limit %2$2s;",input,n).toString();

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
        ArrayList<String> list = new ArrayList<>();
        if(null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        String definition = cursor.getString(0);
                        //Log.i("SQLiteAssetHelperTest",word);
                        list.add(definition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }
}
