package com.baliyaan.android.wordincontext.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.widget.SimpleCursorAdapter;

import com.baliyaan.android.wordincontext.Data.WordListDB;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Pulkit Singh on 6/17/2017.
 */

public class Dictionary {
    private static Dictionary _dictionary = null;
    private SuggestionsAdapter _suggestionsAdapter = null;
    private Context _context = null;
    private WordListDB _db = null;

    private Dictionary(final Context context){
        _context = context;
    }


    public static Dictionary GetInstance(Context context){
        if(null == _dictionary)
            _dictionary = new Dictionary(context);
        return _dictionary;
    }


    public SuggestionsAdapter GetSuggestionsAdapter(Context context){
        if(null == _suggestionsAdapter) {
            _suggestionsAdapter = SuggestionsAdapter.GetInstance(context);
        }
        return _suggestionsAdapter;
    }

    public static class SuggestionsAdapter extends SimpleCursorAdapter {

        private Context _context = null;
        ArrayList<String> _suggestions = null;

        private SuggestionsAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            _context = context;
        }

        protected static SuggestionsAdapter GetInstance(Context context)
        {
            MatrixCursor cursor = new MatrixCursor(new String[] {"_id","word"}); //one column named "_id" is required for CursorAdapter
            String[] from = {"word"};
            int[] to = {android.R.id.text1}; //android.R.id.text1 is a TextView in the android.R.layout.simple_list_item_1 layout
            return new SuggestionsAdapter(context,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    from, to, 0);
        }

        public SuggestionsAdapter SuggestFor(final String token, final int nbrSuggestions)
        {
            final MatrixCursor cursorWithSuggestions = new MatrixCursor(new String[] {"_id","word"}); //one column named "_id" is required for CursorAdapter

            new Observable<String>(){
                @Override
                protected void subscribeActual(Observer<? super String> observer) {
                    if(_suggestions != null)
                        _suggestions.removeAll(_suggestions);

                    _suggestions = Dictionary.GetInstance(_context).GetSuggestionsFor(token,nbrSuggestions);

                    if(_suggestions != null && _suggestions.size()>0) {
                        int key = 1;
                        for (String suggestion : _suggestions) {
                            cursorWithSuggestions.addRow(new Object[]{key, suggestion});
                            key++;
                        }
                    }
                    observer.onNext("");
                }
            }.subscribeOn(Schedulers.newThread())
             .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<String>() {
                @Override
                public void accept(@NonNull String s) throws Exception {
                    changeCursor(cursorWithSuggestions);
                }
            });

            return this;
        }

        public String GetSuggestionAt(int index)
        {
            return _suggestions.get(index);
        }

    }

    private ArrayList<String> GetSuggestionsFor(String token, int nbrSuggestions) {
        if(null == _db)
            _db = new WordListDB(_context);
        return _db.GetWordsStartingWith(token,nbrSuggestions);
    }
}
