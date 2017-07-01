package com.baliyaan.android.wordincontext.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.widget.SimpleCursorAdapter;

import com.baliyaan.android.library.ds.Trie;
import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.Data.WordListDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Pulkit Singh on 6/17/2017.
 */

public class Dictionary {
    private static Dictionary _dictionary = null;
    private SuggestionsAdapter _suggestionsAdapter = null;
    private Context _context = null;
    private Trie _words = null;
    private WordListDB _db = null;

    private Dictionary(final Context context){
        _words = new Trie();
        _context = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoadWordsList(context);
            }
        }).start();
    }

    private void LoadWordsList(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.ukacd17);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line = reader.readLine();
            while(line!=null)
            {
                _words.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Dictionary GetInstance(Context context){
        if(null == _dictionary)
            _dictionary = new Dictionary(context);
        return _dictionary;
    }

    public Trie GetWordsTrie()
    {
        return _words;
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

        public SuggestionsAdapter SuggestFor(String token, int nbrSuggestions)
        {
            if(null != _suggestions)
                _suggestions.removeAll(_suggestions);
            //_suggestions = Dictionary.GetInstance(_context).GetWordsTrie().getWordsStartingWith(token,nbrSuggestions);
            _suggestions = Dictionary.GetInstance(_context).GetSuggestionsFor(token,nbrSuggestions);
            if(_suggestions != null && _suggestions.size()>0)
            {
                MatrixCursor cursorWithSuggestions = new MatrixCursor(new String[] {"_id","word"}); //one column named "_id" is required for CursorAdapter

                this.changeCursor(cursorWithSuggestions);
                int key = 1;
                for(String suggestion : _suggestions)
                {
                    cursorWithSuggestions.addRow(new Object[]{key,suggestion});
                    key++;
                }
            }
            return this;
        }

        public String GetSuggestionAt(int index)
        {
            return _suggestions.get(index);
        }

        public void RemoveCursor()
        {
            this.swapCursor(null);
        }
    }

    private ArrayList<String> GetSuggestionsFor(String token, int nbrSuggestions) {
        if(null == _db)
            _db = new WordListDB(_context);
        return _db.GetWordsStartingWith(token,nbrSuggestions);
    }
}
