package com.baliyaan.android.wordincontext.Components.Definition.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 11/4/2017.
 */

public class DefinitionView extends RelativeLayout {
    public DefinitionView(Context context) {
        super(context);
        init(context);
    }

    public DefinitionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DefinitionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.definition_main,this);
    }

    public void setDefinition(String definition){
        TextView def_tv = (TextView) findViewById(R.id.definition_tv);
        def_tv.setText(definition);
    }
}
