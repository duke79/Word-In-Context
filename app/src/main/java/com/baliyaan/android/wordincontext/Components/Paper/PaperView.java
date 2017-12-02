package com.baliyaan.android.wordincontext.Components.Paper;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.baliyaan.android.wordincontext.R;

import java.util.ArrayList;

/**
 * Created by Pulkit Singh on 11/15/2017.
 */

public class PaperView extends RelativeLayout {
    class PVOnTouchListener implements OnTouchListener {

        int _startX;
        int _startY;
        int _lastX;
        int _lastY;
        private int _maxOffsetForBottomView;
        private float _dragSpeedScaleForPrallax;
        private final VelocityTracker _velocityTracker;
        private final Scroller _scroller;
        private final ValueAnimator _scrollAnimator;
        private final Scroller _scrollerSV;
        private final ValueAnimator _scrollAnimatorSV;
        private boolean _bUpDragAllowed;
        private boolean _bDownDragAllowed;
        private int _lastBottomTop;
        private int TOLERANCE = 2;
        private int _BVtopOnDown;
        private int onTouchLeaveBottomCount = 0;

        PVOnTouchListener() {
            _velocityTracker = VelocityTracker.obtain();
            /*
            * Scroll BottomView and its dependents viz. ParallaxView
            */
            _scroller = new Scroller(getContext());
            _scrollAnimator = ValueAnimator.ofFloat(0, 1);
            _scrollAnimator.setDuration(50000);
            _scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (!_scroller.isFinished()) {
                        _scroller.computeScrollOffset();
                        float scrollerY = _scroller.getCurrY();
                        _bottomView.layout(_bottomView.getLeft(),
                                Math.round(scrollerY),
                                _bottomView.getRight(),
                                Math.round(scrollerY + _bottomView.getMeasuredHeight()));

                        int parallaxTop = Math.round(_dragSpeedScaleForPrallax * (scrollerY - _parallaxView.getMeasuredHeight()));
                        _parallaxView.layout(_parallaxView.getLeft(),
                                parallaxTop,
                                _parallaxView.getRight(),
                                parallaxTop + _parallaxView.getMeasuredHeight());

                    } else {
                        _scrollAnimator.cancel();
                    }
                }
            });

            /*
            * Scroll SearchView
            */
            _scrollerSV = new Scroller(getContext());
            _scrollAnimatorSV = ValueAnimator.ofFloat(0, 1);
            _scrollAnimatorSV.setDuration(50000);
            _scrollAnimatorSV.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (!_scrollerSV.isFinished()) {
                        _scrollerSV.computeScrollOffset();
                        float scrollerY = _scrollerSV.getCurrY();
                        _fullSearchBar.layout(_fullSearchBar.getLeft(),
                                Math.round(scrollerY),
                                _fullSearchBar.getRight(),
                                Math.round(scrollerY + _fullSearchBar.getMeasuredHeight()));
                    } else {
                        _scrollAnimatorSV.cancel();
                    }
                }
            });

            /*
            * Initialize DragSpeedScale relative to BottomView
             */
            _maxOffsetForBottomView = getMeasuredHeight() - _bottomView.getMeasuredHeight() - _parallaxView.getMeasuredHeight();
            _dragSpeedScaleForPrallax = Math.abs((float) (getMeasuredHeight() - _bottomView.getMeasuredHeight()) / _maxOffsetForBottomView);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            /*
            * Fling - if ACTION_UP
            */
            _velocityTracker.addMovement(event);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                _velocityTracker.computeCurrentVelocity(1000);
                if (_velocityTracker.getYVelocity() > 0 && _bDownDragAllowed) {
                    scrollToBottom();
                    onTouchBottom();
                } else if (_velocityTracker.getYVelocity() < 0 && _bUpDragAllowed) {
                    scrollToMid();
                    //onLeaveBottom();
                } else if ((_bUpDragAllowed || _bDownDragAllowed)
                        && (_BVtopOnDown == _bottomView.getTop())) {
                    int upwardDragRequired = _parallaxView.getMeasuredHeight() - _bottomView.getTop();
                    int downwardDragRequired = getMeasuredHeight() - _bottomView.getBottom();
                    if (upwardDragRequired < downwardDragRequired && _bUpDragAllowed) {
                        scrollToMid();
                        onLeaveBottom();
                    } else if (_bDownDragAllowed) {
                        scrollToBottom();
                        onTouchBottom();
                    }
                }
            }

            /*
            * Drag if ACTION_MOVE
             */
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                _startX = Math.round(event.getX());
                _startY = Math.round(event.getY());

                int initialBottomTop = _bottomView.getTop();
                int initialBottomBottom = _bottomView.getBottom();
                _bUpDragAllowed = initialBottomTop > _parallaxView.getMeasuredHeight();
                _bDownDragAllowed = initialBottomBottom < getMeasuredHeight();
                _lastBottomTop = _bottomView.getTop();
                _BVtopOnDown = _bottomView.getTop();
            } else if (MotionEvent.ACTION_MOVE == event.getAction()) {
                int currentX = Math.round(event.getX());
                int currentY = Math.round(event.getY());
                int offsetX = currentX - _lastX;
                int offsetY = currentY - _lastY;
                boolean bDraggingUp = offsetY < 0;

                if (!_bDownDragAllowed) {
                    int expectedBVtop = getMeasuredHeight() - _bottomView.getMeasuredHeight();
                    if (bDraggingUp)
                        onLeaveBottom();
                    else if (_lastBottomTop < expectedBVtop && offsetY!=0)// - TOLERANCE)
                        onTouchBottom();
                }

                if ((bDraggingUp && _bUpDragAllowed)
                        || (!bDraggingUp && _bDownDragAllowed)) {
                    _lastBottomTop = _bottomView.getTop();
                    updatePVLayout(event, offsetX, offsetY);
                    _bUpDragAllowed = _bottomView.getTop() > _parallaxView.getMeasuredHeight();
                    _bDownDragAllowed = _bottomView.getBottom() < getMeasuredHeight();
                }
            }

            /*
            * Store last position of touch, to help drag
             */
            _lastX = Math.round(event.getX());
            _lastY = Math.round(event.getY());

            return true;
        }

        private void updatePVLayout(MotionEvent event, int offsetX, int offsetY) {
            /*
            * Vertically offset the views
             */
            int iBVresultBottom = _bottomView.getBottom() + offsetY;
            int iHeight = getMeasuredHeight();
            int iBVresultTop = _bottomView.getTop() + offsetY;
            int iPVHeight = _parallaxView.getMeasuredHeight();
            if (iBVresultBottom <= iHeight
                    && iBVresultTop >= iPVHeight) {
                _bottomView.offsetTopAndBottom(offsetY);
                int offsetForParallax = Math.round(_dragSpeedScaleForPrallax * (offsetY));
                _parallaxView.offsetTopAndBottom(offsetForParallax);
            }
        }

        boolean _bBottomTouchedInLastEvent = false;

        private void onTouchBottom() {
            /*
            * Scroll the SearchBar into view
            */
            if(onTouchLeaveBottomCount>0) {
                int scrollStart = _fullSearchBar.getTop();
                ViewGroup.MarginLayoutParams lp = (MarginLayoutParams) _fullSearchBar.getLayoutParams();
                int scrollEnd = _fullSearchBar.getMeasuredHeight() + lp.topMargin + SAFE_DISTANCE_TO_HIDE_VIEWS;
                _scrollerSV.startScroll(0, scrollStart, 0, scrollEnd);
                _scrollAnimatorSV.start();
                onTouchLeaveBottomCount--;
            }
        }

        private void onLeaveBottom() {
            /*
            * Scroll the SearchBar out of view
            */
            if(onTouchLeaveBottomCount<=0) {
                int scrollStart = _fullSearchBar.getTop();
                ViewGroup.MarginLayoutParams lp = (MarginLayoutParams) _fullSearchBar.getLayoutParams();
                int scrollEnd = -_fullSearchBar.getMeasuredHeight() - lp.topMargin - SAFE_DISTANCE_TO_HIDE_VIEWS;
                _scrollerSV.startScroll(0, scrollStart, 0, scrollEnd);
                _scrollAnimatorSV.start();
                onTouchLeaveBottomCount++;
            }
        }

        private void scrollToBottom() {
            int scrollStart = _bottomView.getTop();
            int scrollEnd = getMeasuredHeight() - _bottomView.getMeasuredHeight() - scrollStart;
            _scroller.startScroll(0, scrollStart, 0, scrollEnd);
            _scrollAnimator.start();
        }

        private void scrollToMid() {
            int scrollStart = _bottomView.getTop();
            int scrollEnd = _parallaxView.getMeasuredHeight() - _bottomView.getTop();
            _scroller.startScroll(0, scrollStart, 0, scrollEnd);
            _scrollAnimator.start();
        }
    }

    /*
    * View Attributes
     */
    // Enums etc (other non-changing values)
    //float SENSITIVITY = 1f;
    double GOLDEN_RATIO = 1.61803398875;
    int GOLDEN_HEIGHT;
    private int SAFE_DISTANCE_TO_HIDE_VIEWS = 300;
    // Helper objects
    GestureDetector _gestureDetector;
    ViewDragHelper _dragHelper;
    PVOnTouchListener _onTouchListener;
    // Child views
    private int _fullSearchBarId;
    private int _minimalSearchBarId;
    private int _bottomViewId;
    private int _parallaxViewId;
    private int _titleViewId;
    private int _contentViewId;
    View _fullSearchBar;
    View _minimalSearchBar;
    View _bottomView;
    View _parallaxView;
    View _titleView;
    View _contentView;

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
        initializeAttributes(context, attrs);
    }

    private void initializeAttributes(Context context, AttributeSet attrs) {
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
            _parallaxViewId = a.getResourceId(R.styleable.PaperView_parallax_view_id, 0);
            _titleViewId = a.getResourceId(R.styleable.PaperView_parallax_view_id, 0);
            _contentViewId = a.getResourceId(R.styleable.PaperView_content_view_id, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mapViews();
        _dragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }
        });
        
        _gestureDetector = new GestureDetector(getContext(), new PaperViewGestureDetector(this));
    }

    private void mapViews() {
        _fullSearchBar = findViewById(_fullSearchBarId);
        _minimalSearchBar = findViewById(_minimalSearchBarId);
        _bottomView = findViewById(_bottomViewId);
        _parallaxView = findViewById(_parallaxViewId);
        _titleView = findViewById(_titleViewId);
        _contentView = findViewById(_contentViewId);
    }

    /*
    * Behavior
     */

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean bShouldProcessTouchEvent = shoudProcessTouchEvent(event);
        if (bShouldProcessTouchEvent) {
            //_gestureDetector.onTouchEvent(event);
            _onTouchListener.onTouch(this, event);
        }
        return super.dispatchTouchEvent(event) || bShouldProcessTouchEvent;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        layoutFirstPosition(l, t, r, b);
        GOLDEN_HEIGHT = getMeasuredHeight() - (int) Math.round(getMeasuredHeight() / GOLDEN_RATIO);
        _onTouchListener = new PVOnTouchListener();
    }

    private void layoutFirstPosition(int l, int t, int r, int b) {
        int heightBottomView = _bottomView.getMeasuredHeight();
        int widthBottomView = _bottomView.getMeasuredWidth();
        int heightFullSearchBar = _fullSearchBar.getMeasuredHeight();
        int widthFullSearchBar = _fullSearchBar.getMeasuredWidth();
        int heightParallexView = _parallaxView.getMeasuredHeight();
        int widthParallexView = _parallaxView.getMeasuredWidth();

        _bottomView.layout(0, b - heightBottomView, r, b);
        ViewGroup.MarginLayoutParams lp = (MarginLayoutParams) _fullSearchBar.getLayoutParams();
        _fullSearchBar.layout(0, lp.topMargin, r, lp.topMargin + heightFullSearchBar);
        _parallaxView.layout(0, b - heightBottomView, r, b - heightBottomView + heightParallexView);
    }

    void setViewSize(float offsetX, float offsetY) {
        ViewGroup.LayoutParams params = getLayoutParams();
        //params.width = 300 + Math.round(offsetX) * 5;
        int parentHeight = ((View) getParent()).getHeight();
        params.height = Math.round(parentHeight * offsetY / 100);
        setLayoutParams(params);
    }

    private ArrayList<View> _viewsUnderTouchOnDown;
    boolean _bShoudProcessTouchEvent = false;

    public boolean shoudProcessTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            float x = ev.getX();
            float y = ev.getY();

            _viewsUnderTouchOnDown = new ArrayList<>();


            /*int nbrChildren = getChildCount();
            for (int i = 0; i < nbrChildren; i++) {
                View child = getChildAt(i);
                if (_dragHelper.isViewUnder(child, Math.round(x), Math.round(y)))
                    _viewsUnderTouchOnDown.add(child);

            }*/
            if (_dragHelper.isViewUnder(_bottomView, Math.round(x), Math.round(y)))
                _viewsUnderTouchOnDown.add(_bottomView);
            if (_dragHelper.isViewUnder(_parallaxView, Math.round(x), Math.round(y)))
                _viewsUnderTouchOnDown.add(_parallaxView);


            if (_viewsUnderTouchOnDown.size() > 0)
                _bShoudProcessTouchEvent = true;
            else
                _bShoudProcessTouchEvent = false;
        }
        return _bShoudProcessTouchEvent;
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

    public int get_parallaxViewId() {
        return _parallaxViewId;
    }

    public void set_parallaxViewId(int _parallaxViewId) {
        this._parallaxViewId = _parallaxViewId;
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