package com.baliyaan.android.wordincontext.UI.Examples;

import android.content.Context;
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

import com.baliyaan.android.wordincontext.MainActivity;
import com.baliyaan.android.wordincontext.R;
import com.baliyaan.android.wordincontext.Model.WordExample;

import java.util.List;

/**
 * Created by Pulkit Singh on 10/15/2016.
 */

public class ExamplesAdapter extends PagerAdapter{
    private List<WordExample> wordExampleList;
    private Context _context = null;

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(_context);
        View wordExamplePage = inflater.inflate(R.layout.word_example,container,false);
        SetPageValues(wordExamplePage,position);
        container.addView(wordExamplePage);
        return wordExamplePage;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private void SetPageValues(View page, int position) {
        TextView content, link, counter;
        content = (TextView) page.findViewById(R.id.content);
        link = (TextView) page.findViewById(R.id.link);
        counter = (TextView) page.findViewById(R.id.counter);

        WordExample wordExample = wordExampleList.get(position);
        content.setText(wordExample._content);
        link.setText(wordExample._link);
        counter.setText("#"+Integer.toString(position+1));

        String query = ((MainActivity) _context)._query;
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
                    _context.startActivity(Intent.createChooser(sharingIntent, "Share"));
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

    public ExamplesAdapter(Context context, List<WordExample> iList)
    {
        this.wordExampleList = iList;
        _context = context;
    }

    @Override
    public int getCount() {
        return wordExampleList.size();
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
