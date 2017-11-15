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
    /*
    * Member variables
     */
    private float _bottomViewHeight;
    private float _topViewHeight;

    /*
    * Initialization
     */
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

    /*
    * Behavior
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /*
    * Attributes' setters & getters
     */
    public float getTopViewHeight(){
        return _topViewHeight;
    }

    public void setTopViewHeight(float height){
        _topViewHeight = height;
    }

    public float getBottomViewHeight(){
        return _bottomViewHeight;
    }

    public void setBottomViewHeight(float height){
        _bottomViewHeight = height;
    }
}
