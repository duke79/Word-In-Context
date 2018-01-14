package com.baliyaan.android.wordincontext.Components.Definition.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.baliyaan.android.library.db.SQLiteAssetHelper.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Pulkit Singh on 1/13/2018.
 */

public class OfflineDictionary extends SQLiteAssetHelper {
    private static OfflineDictionary _dict = null;
    private static final String DATABASE_NAME = "Dictionary.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase _db = null;

    private OfflineDictionary(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    public static OfflineDictionary GetInstance(Context context)
    {
        if(null == _dict)
            _dict = new OfflineDictionary(context);
        return _dict;
    }

    public ArrayList<String> GetWordsStartingWith(String input, int n) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        String query = formatter.format("select word from entries where word like \"%1$1s%%\" limit %2$2s;",input,n).toString();

        //Todo: Will aslo need definition, right?
        //select definition from entries where word like "%1$1s%%" limit %2$2s;

        Cursor cursor = null;
        try {
            if(null == _db)
                _db = getWritableDatabase();
            cursor = _db.rawQuery(query,null);
        }
        catch (Exception e){
            Log.e("WordListDB",e.toString());
        }
        //Traverse cursor
        ArrayList<String> list = new ArrayList<>();
        if(null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    String word = cursor.getString(cursor.getColumnIndex("word"));
                    //Log.i("SQLiteAssetHelperTest",word);
                    list.add(word);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }
}
