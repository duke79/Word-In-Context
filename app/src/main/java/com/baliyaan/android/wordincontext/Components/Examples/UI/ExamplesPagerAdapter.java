package com.baliyaan.android.wordincontext.Components.Examples.UI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baliyaan.android.wordincontext.Components.Examples.MVP.ExamplesMVPContract;
import com.baliyaan.android.wordincontext.Components.Examples.Model.Example;
import com.baliyaan.android.wordincontext.R;

import java.util.List;

/**
 * Created by Pulkit Singh on 10/15/2016.
 */

public class ExamplesPagerAdapter extends PagerAdapter{
    private List<Example> exampleList;
    private ExamplesMVPContract.Navigator _navigator = null;

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(_navigator.getContext());
        View wordExamplePage = inflater.inflate(R.layout.examples_word_example,container,false);
        SetPageValues(wordExamplePage,position);
        container.addView(wordExamplePage);
        return wordExamplePage;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private void SetPageValues(View page, int position) {
        TextView content, link;
        content = (TextView) page.findViewById(R.id.content);
        link = (TextView) page.findViewById(R.id.link);

        Example example = exampleList.get(position);
        content.setText(example._content);
        link.setText(example._link);

        String query = _navigator.getQuery();
        HighLightQueryString(content,query);

        SetOnSelectionIntent(content);
    }

    private void SetOnSelectionIntent(final TextView tv) {
        tv.setCustomSelectionActionModeCallback(new ActionMode.Callback(){
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                menu.add("Share").setIcon(R.drawable.ic_share);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                String title = item.getTitle().toString();
                if(title.contains("Share"))
                {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    int start = tv.getSelectionStart();
                    int end = tv.getSelectionEnd();
                    String shareBody = tv.getText().toString().substring(start,end);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    _navigator.getContext().startActivity(Intent.createChooser(sharingIntent, "Share"));
                }
                return false;
            }
        });
    }

    private void HighLightQueryString(TextView contentView, String query) {
        String content = contentView.getText().toString();
        Spannable wordToSpan = new SpannableString(content);
        for (int start = 0; start != -1; ) {
            query = query.toLowerCase();
            content = content.toLowerCase();
            start = content.indexOf(query, start+1);

            if (start != -1) {
                int end = start + query.length();
                wordToSpan.setSpan(new ForegroundColorSpan(Color.DKGRAY), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else
                break;
        }

        contentView.setText(wordToSpan);
    }

    public ExamplesPagerAdapter(ExamplesMVPContract.Navigator navigator, List<Example> iList)
    {
        this.exampleList = iList;
        _navigator = navigator;
    }

    @Override
    public int getCount() {
        return exampleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "PageTitle";
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
