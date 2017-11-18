package com.baliyaan.android.wordincontext.Components.Paper;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 11/15/2017.
 */

public class PaperView extends RelativeLayout {
    /*
    * View Attributes
     */
    // Enums etc (other non-changing values)
    int DENSITY_INDEPENDENT_THRESHOLD;
    int SWIPE_THRESHOLD_VELOCITY;
    float SENSITIVITY = 1f;
    // Helper objects
    private GestureDetector _gestureDetector;
    private ViewDragHelper _dragHelper;
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

        /*
        * One time initialization
         */
        DENSITY_INDEPENDENT_THRESHOLD = 200;
        float density = getResources().getDisplayMetrics().density;
        SWIPE_THRESHOLD_VELOCITY = (int) (DENSITY_INDEPENDENT_THRESHOLD * density);
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
        _gestureDetector = new GestureDetector(getContext(), new PaperViewGestureDetector(this));
        _dragHelper = ViewDragHelper.create(this,SENSITIVITY, new DragHelper(this));
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldIntercept =  _dragHelper.shouldInterceptTouchEvent(ev);
        return shouldIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = _gestureDetector.onTouchEvent(event);
        _dragHelper.processTouchEvent(event);
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        layoutFirstPosition(l,t,r,b);
    }

    private void layoutFirstPosition(int l, int t, int r, int b) {
        int heightBottomView = _bottomView.getMeasuredHeight();
        int widthBottomView = _bottomView.getMeasuredWidth();
        int heightFullSearchBar = _fullSearchBar.getMeasuredHeight();
        int widthFullSearchBar = _fullSearchBar.getMeasuredWidth();
        int heightParallexView = _parallaxView.getMeasuredHeight();
        int widthParallexView = _parallaxView.getMeasuredWidth();
        
        _bottomView.layout(0,b-heightBottomView,r,b);
        _fullSearchBar.layout(0,100,r,100+heightFullSearchBar);
        _parallaxView.layout(0,b,r,b+heightParallexView);
    }

    void setViewSize(float offsetX, float offsetY) {
        ViewGroup.LayoutParams params = getLayoutParams();
        //params.width = 300 + Math.round(offsetX) * 5;
        int parentHeight = ((View) getParent()).getHeight();
        params.height = Math.round(parentHeight * offsetY / 100);
        setLayoutParams(params);
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