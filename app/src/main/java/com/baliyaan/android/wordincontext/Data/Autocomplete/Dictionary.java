package com.baliyaan.android.wordincontext.Data.Autocomplete;

import android.content.Context;

import com.baliyaan.android.wordincontext.Data.WordListDB;

import java.util.ArrayList;

/**
 * Created by Pulkit Singh on 6/17/2017.
 */

class Dictionary {

    private static Dictionary _dictionary = null;
    private WordListDB _db = null;

    private Dictionary(final Context context){
        _db = new WordListDB(context);
    }

    static Dictionary getInstance(Context context){
        if(null == _dictionary)
            _dictionary = new Dictionary(context);
        return _dictionary;
    }

    ArrayList<String> getSuggestionsFor(String token, int nbrSuggestions) {
        return _db.GetWordsStartingWith(token,nbrSuggestions);
    }
}
