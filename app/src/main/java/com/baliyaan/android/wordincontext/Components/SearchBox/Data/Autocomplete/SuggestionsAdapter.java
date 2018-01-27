package com.baliyaan.android.wordincontext.Components.SearchBox.Data.Autocomplete;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.widget.SimpleCursorAdapter;

import com.baliyaan.android.wordincontext.Data.Dictionary;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public class SuggestionsAdapter extends SimpleCursorAdapter {

    private Dictionary _dictionary = null;
    private ArrayList<String> _suggestions = null;

    private SuggestionsAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        _dictionary = Dictionary.getInstance(context);
    }

    public static SuggestionsAdapter getInstance(Context context) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "word"}); //one column named "_id" is required for CursorAdapter
        String[] from = {"word"};
        int[] to = {android.R.id.text1}; //android.R.id.text1 is a TextView in the android.R.layout.simple_list_item_1 layout
        return new SuggestionsAdapter(context,
                android.R.layout.simple_list_item_1,
                cursor,
                from, to, 0);

    }

    private String _token = "";
    public SuggestionsAdapter suggestFor(final String token, final int nbrSuggestions) {
        _token = token;

        final MatrixCursor cursorWithSuggestions = new MatrixCursor(new String[]{"_id", "word"}); //one column named "_id" is required for CursorAdapter

        _dictionary.getSuggestionsFor(token, nbrSuggestions)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<String>>() {
                    @Override
                    public void accept(@NonNull ArrayList<String> suggestions) throws Exception {
                        if (!_token.equals(token))
                            return;

                        if (_suggestions != null)
                            _suggestions.removeAll(_suggestions);
                        _suggestions = suggestions;

                        if (_suggestions != null && _suggestions.size() > 0) {
                            int key = 1;
                            for (String suggestion : _suggestions) {
                                cursorWithSuggestions.addRow(new Object[]{key, suggestion});
                                key++;
                            }
                        }
                        changeCursor(cursorWithSuggestions);
                    }
                });

        return this;
    }

    public String getSuggestionAt(int index) {
        return _suggestions.get(index);
    }

}