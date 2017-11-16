package com.baliyaan.android.wordincontext.Components.Paper;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 11/15/2017.
 */

public class PaperView extends RelativeLayout {
    /*
    * View Attributes
     */
    private float _bottomViewHeight;
    private float _topViewHeight;
    /*
    * Enums etc (other non-changing values)
     */
    int SCALE = 2;
    /*
    * Helper objects
     */
    private GestureDetector _gestureDetector;
    private Scroller _scroller;
    private ValueAnimator _scrollAnimator;

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
        /*
        * Read attributes
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PaperView,
                0, 0);

        try {
            _bottomViewHeight = a.getDimension(R.styleable.PaperView_bottomViewHeight, 0);
            _topViewHeight = a.getDimension(R.styleable.PaperView_topViewHeight, 0);
        } finally {
            a.recycle();
        }

        /*
        * One time initialization
         */
        _gestureDetector = new GestureDetector(getContext(), new PaperViewGestureDetector());
        _scroller = new Scroller(getContext(),null,true);
        _scrollAnimator = ValueAnimator.ofFloat(0,1);
        _scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(!_scroller.isFinished()){
                    _scroller.computeScrollOffset();
                    float offsetX = _scroller.getCurrX();
                    float offsetY = _scroller.getCurrY();
                    setViewSize(offsetX,offsetY);
                } else {
                    _scrollAnimator.cancel();
                }
            }
        });
    }

    private void setViewSize(float offsetX, float offsetY) {
        ViewGroup.LayoutParams params =  getLayoutParams();
        params.width = 300+Math.round(offsetX)*5;
        params.height = 500+Math.round(offsetY)*8;
        setLayoutParams(params);
    }

    /*
    * Behavior
     */
    class PaperViewGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            _scroller.fling(0,0,Math.round(velocityX/SCALE),Math.round(velocityY/SCALE),0,100,0,100);
            _scrollAnimator.start();
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = _gestureDetector.onTouchEvent(event);
        if (!result) {
            if(event.getAction() == MotionEvent.ACTION_UP){
                result = true;
            }
        }
        return result;
    }

    /*
    * Attributes' setters & getters
     */
    public float getTopViewHeight() {
        return _topViewHeight;
    }

    public void setTopViewHeight(float height) {
        _topViewHeight = height;
    }

    public float getBottomViewHeight() {
        return _bottomViewHeight;
    }

    public void setBottomViewHeight(float height) {
        _bottomViewHeight = height;
    }
}
