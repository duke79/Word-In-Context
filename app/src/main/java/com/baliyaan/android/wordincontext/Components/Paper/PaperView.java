package com.baliyaan.android.wordincontext.Components.Paper;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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
    //private float _bottomViewHeight;
    //private float _topViewHeight;
    /*
    * Enums etc (other non-changing values)
     */
    private int DENSITY_INDEPENDENT_THRESHOLD;
    private int SWIPE_THRESHOLD_VELOCITY;
    /*
    * Helper objects
     */
    private GestureDetector _gestureDetector;
    private Scroller _scroller;
    private ValueAnimator _scrollAnimator;
    /*
    * Child views
     */
    private int _fullSearchBarId;
    private int _minimalSearchBarId;
    private int _bottomViewId;
    private int _parallexViewId;
    private int _titleViewId;
    private int _contentViewId;
    private View _fullSearchBar;
    private View _minimalSearchBar;
    private View _bottomView;
    private View _parallexView;
    private View _titleView;
    private View _contentView;

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
        initializeAttribute(context, attrs);

        /*
        * One time initialization
         */
        _gestureDetector = new GestureDetector(getContext(), new PaperViewGestureDetector());
        _scroller = new Scroller(getContext());
        _scrollAnimator = ValueAnimator.ofFloat(0, 1);
        _scrollAnimator.setDuration(50000);
        _scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!_scroller.isFinished()) {
                    _scroller.computeScrollOffset();
                    float offsetX = _scroller.getCurrX();
                    float offsetY = _scroller.getCurrY();
                    setViewSize(offsetX, offsetY);
                } else {
                    float offsetX = _scroller.getCurrX();
                    float offsetY = _scroller.getCurrY();
                    _scrollAnimator.cancel();
                }
            }
        });
        DENSITY_INDEPENDENT_THRESHOLD = 200;
        float density = getResources().getDisplayMetrics().density;
        SWIPE_THRESHOLD_VELOCITY = (int) (DENSITY_INDEPENDENT_THRESHOLD * density);
    }

    private void initializeAttribute(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PaperView,
                0, 0);

        try {
            /*
            * Get child views' IDs
             */
            _fullSearchBarId = a.getResourceId(R.styleable.PaperView_full_search_bar_id, 0);
            _minimalSearchBarId = a.getResourceId(R.styleable.PaperView_minimal_search_bar_id, 0);
            _bottomViewId = a.getResourceId(R.styleable.PaperView_bottom_view_id, 0);
            _parallexViewId = a.getResourceId(R.styleable.PaperView_parallex_view_id, 0);
            _titleViewId = a.getResourceId(R.styleable.PaperView_parallex_view_id, 0);
            _contentViewId = a.getResourceId(R.styleable.PaperView_content_view_id, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mapViews();
    }

    private void mapViews() {
        _fullSearchBar = findViewById(_fullSearchBarId);
        _minimalSearchBar = findViewById(_minimalSearchBarId);
        _bottomView = findViewById(_bottomViewId);
        _parallexView = findViewById(_parallexViewId);
        _titleView = findViewById(_titleViewId);
        _contentView = findViewById(_contentViewId);
    }

    private void setViewSize(float offsetX, float offsetY) {
        ViewGroup.LayoutParams params = getLayoutParams();
        //params.width = 300 + Math.round(offsetX) * 5;
        int parentHeight = ((View) getParent()).getHeight();
        params.height = Math.round(parentHeight * offsetY / 100);
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
            if (velocityY > SWIPE_THRESHOLD_VELOCITY) {
                _scroller.fling(0, 0, 1000 + Math.round(velocityX), 1000 + Math.round(velocityY), 0, 100, 0, 100);
                _scrollAnimator.start();
                return true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = _gestureDetector.onTouchEvent(event);
        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                result = true;
            }
        }
        return result;
    }

    /*
    * Attributes' setters & getters
     */
    public int get_fullSearchBarId() {
        return _fullSearchBarId;
    }

    public void set_fullSearchBarId(int _fullSearchBarId) {
        this._fullSearchBarId = _fullSearchBarId;
    }

    public int get_minimalSearchBarId() {
        return _minimalSearchBarId;
    }

    public void set_minimalSearchBarId(int _minimalSearchBarId) {
        this._minimalSearchBarId = _minimalSearchBarId;
    }

    public int get_bottomViewId() {
        return _bottomViewId;
    }

    public void set_bottomViewId(int _bottomViewId) {
        this._bottomViewId = _bottomViewId;
    }

    public int get_parallexViewId() {
        return _parallexViewId;
    }

    public void set_parallexViewId(int _parallexViewId) {
        this._parallexViewId = _parallexViewId;
    }

    public int get_titleViewId() {
        return _titleViewId;
    }

    public void set_titleViewId(int _titleViewId) {
        this._titleViewId = _titleViewId;
    }

    public int get_contentViewId() {
        return _contentViewId;
    }

    public void set_contentViewId(int _contentViewId) {
        this._contentViewId = _contentViewId;
    }
}