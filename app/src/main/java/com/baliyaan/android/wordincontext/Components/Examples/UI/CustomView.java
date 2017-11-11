package com.baliyaan.android.wordincontext.Components.Examples.UI;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baliyaan.android.wordincontext.R;
import com.rd.PageIndicatorView;

import static com.baliyaan.android.wordincontext.R.id.pageIndicatorView;


/**
 * Created by Pulkit Singh on 7/5/2017.
 */

public class CustomView extends RelativeLayout {

    private TextView _textView;
    private ViewPager _viewPager;
    private PagerAdapter _adapter;
    private PageIndicatorView _pageIndicatorView;

    public CustomView(Context context) {
        super(context);
        init(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.examples_main,this);
        displayWelcomeText();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        _textView = (TextView) findViewById(R.id.welcome_text);
        _viewPager = (ViewPager) findViewById(R.id.view_pager);
        _pageIndicatorView = (PageIndicatorView) findViewById(pageIndicatorView);
        _pageIndicatorView.setViewPager(_viewPager);
        _pageIndicatorView.setDynamicCount(true);
        _pageIndicatorView.setSelectedColor(Color.parseColor("#000000"));
        _pageIndicatorView.setUnselectedColor(Color.parseColor("#999999"));
    }

    public void setAdapter(PagerAdapter adapter){
        _adapter = adapter;
        _viewPager.setAdapter(adapter);
    }

    public void displayList(){
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        findViewById(R.id.welcome_text).setVisibility(View.GONE);
        findViewById(R.id.error_text).setVisibility(View.GONE);
        findViewById(R.id.view_pager).setVisibility(View.VISIBLE);
        findViewById(pageIndicatorView).setVisibility(View.VISIBLE);


        _adapter.notifyDataSetChanged();
        _viewPager.setCurrentItem(0);
    }

    public void displayLoading(){
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.welcome_text).setVisibility(View.GONE);
        findViewById(R.id.error_text).setVisibility(View.GONE);
        findViewById(R.id.view_pager).setVisibility(View.GONE);
        findViewById(pageIndicatorView).setVisibility(View.GONE);
    }

    public void displayWelcomeText(){
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        findViewById(R.id.welcome_text).setVisibility(View.VISIBLE);
        findViewById(R.id.error_text).setVisibility(View.GONE);
        findViewById(R.id.view_pager).setVisibility(View.GONE);
        findViewById(pageIndicatorView).setVisibility(View.GONE);
    }

    public void displayErrorText(){
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        findViewById(R.id.welcome_text).setVisibility(View.GONE);
        findViewById(R.id.error_text).setVisibility(View.VISIBLE);
        findViewById(R.id.view_pager).setVisibility(View.GONE);
        findViewById(pageIndicatorView).setVisibility(View.GONE);
    }

    public void setVisibilityForChildren(int visibility){
        int nbrChildren = getChildCount();
        for (int index=0;index<nbrChildren;index++) {
            View child = getChildAt(index);
            child.setVisibility(visibility);
        }

    }
}
