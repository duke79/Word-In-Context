package com.baliyaan.android.wordincontext.Components.Definition;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 11/4/2017.
 */

public class UIDefinitionView  extends RelativeLayout {
    public UIDefinitionView(Context context) {
        super(context);
        init(context);
    }

    public UIDefinitionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UIDefinitionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ui_definition_view,this);
    }
}
