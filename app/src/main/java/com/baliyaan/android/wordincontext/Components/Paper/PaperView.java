package com.baliyaan.android.wordincontext.Components.Paper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Pulkit Singh on 11/15/2017.
 */

public class PaperView extends RelativeLayout{
    public PaperView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PaperView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
