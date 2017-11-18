package com.baliyaan.android.wordincontext.Components.Paper;

import android.animation.ValueAnimator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Scroller;

/**
 * Created by Pulkit Singh on 11/18/2017.
 */

class PaperViewGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private PaperView _paperView;
    private Scroller _scroller;
    private ValueAnimator _scrollAnimator;

    PaperViewGestureDetector(PaperView paperView){
        _paperView = paperView;

        _scroller = new Scroller(_paperView.getContext());
        _scrollAnimator = ValueAnimator.ofFloat(0, 1);
        _scrollAnimator.setDuration(50000);
        _scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!_scroller.isFinished()) {
                    _scroller.computeScrollOffset();
                    float offsetX = _scroller.getCurrX();
                    float offsetY = _scroller.getCurrY();
                    _paperView.setViewSize(offsetX, offsetY);
                } else {
                    float offsetX = _scroller.getCurrX();
                    float offsetY = _scroller.getCurrY();
                    _scrollAnimator.cancel();
                }
            }
        });
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (velocityY > _paperView.SWIPE_THRESHOLD_VELOCITY) {
            _scroller.fling(0, 0, 1000 + Math.round(velocityX), 1000 + Math.round(velocityY), 0, 100, 0, 100);
            _scrollAnimator.start();
            return true;
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}