package com.baliyaan.android.wordincontext.Components.Definition.UI;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 11/4/2017.
 */

public class CustomView extends LinearLayout {
    private RecyclerView _recyclerView;

    public CustomView(Context context) {
        super(context);
        init(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.definitions,this);
        _recyclerView = (RecyclerView) findViewById(R.id.definitions_list);
        //_recyclerView.setHasFixedSize(false);
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        _recyclerView.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        //lm.setOrientation(VERTICAL);
        _recyclerView.setLayoutManager(lm);
    }
}
