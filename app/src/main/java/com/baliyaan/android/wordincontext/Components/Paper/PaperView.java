package com.baliyaan.android.wordincontext.Components.Paper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 11/15/2017.
 */

public class PaperView extends RelativeLayout {
    private float _bottomViewHeight;
    private float _topViewHeight;

    public PaperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public PaperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PaperView(Context context) {
        super(context);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PaperView,
                0, 0);

        try {
            _bottomViewHeight = a.getDimension(R.styleable.PaperView_bottomViewHeight, 0);
            _topViewHeight = a.getDimension(R.styleable.PaperView_topViewHeight, 0);
        }
        finally {
            a.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
