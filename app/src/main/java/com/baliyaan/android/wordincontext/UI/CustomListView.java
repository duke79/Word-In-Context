package com.baliyaan.android.wordincontext.UI;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baliyaan.android.wordincontext.R;

/**
 * Created by Pulkit Singh on 7/5/2017.
 */

public class CustomListView extends RelativeLayout {

    private TextView _textView;
    private ViewPager _viewPager;
    private PagerAdapter _adapter;

    public CustomListView(Context context) {
        super(context);
        init(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.expandable_list_view,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        _textView = (TextView) findViewById(R.id.welcome_text);
        _viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    public void setAdapter(PagerAdapter adapter){
        _adapter = adapter;
        _viewPager.setAdapter(adapter);
    }

    public void displayList(){
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        findViewById(R.id.welcome_text).setVisibility(View.GONE);
        findViewById(R.id.view_pager).setVisibility(View.VISIBLE);

        _adapter.notifyDataSetChanged();
        _viewPager.setCurrentItem(0);
    }

    public void displayLoading(){
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.welcome_text).setVisibility(View.GONE);
        findViewById(R.id.view_pager).setVisibility(View.GONE);
    }

    public void displayWelcomeText(){
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        findViewById(R.id.welcome_text).setVisibility(View.VISIBLE);
        findViewById(R.id.view_pager).setVisibility(View.GONE);
    }
}
