package com.baliyaan.android.wordincontext.Components.Definition.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 11/4/2017.
 */

public class CustomView extends LinearLayout {
    private LayoutInflater _inflater;

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
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addDefinition(String definition){
        ViewGroup view = (ViewGroup) _inflater.inflate(R.layout.definition_main,this);
        TextView def_tv = (TextView) ((ViewGroup)view.getChildAt(getChildCount()-1)).getChildAt(2);
        def_tv.setText(definition);
    }
}
