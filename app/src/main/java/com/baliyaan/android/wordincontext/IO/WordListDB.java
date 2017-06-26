package com.baliyaan.android.wordincontext.IO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.baliyaan.android.library.db.SQLiteAssetHelper.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Pulkit Singh on 6/26/2017.
 */

public class WordListDB extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "WordsList.db";
    private static final int DATABASE_VERSION = 1;

    public WordListDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    public ArrayList<String> GetWordsStartingWith(String input, int n) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        String query = formatter.format("select word from WordsList where word like \"%1$1s%%\" limit %2$2s;",input,n).toString();

        Cursor cursor = null;
        try {
            SQLiteDatabase db = getWritableDatabase();
            cursor = db.rawQuery(query,null);
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
