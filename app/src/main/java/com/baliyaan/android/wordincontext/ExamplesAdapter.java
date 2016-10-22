package com.baliyaan.android.wordincontext;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pulkit Singh on 10/15/2016.
 */

public class ExamplesAdapter extends RecyclerView.Adapter<ExamplesAdapter.MyViewHolder> {
    private List<WordExample> wordExampleList;
    Context _context = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView content, link, counter;

        public MyViewHolder(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.content);
            link = (TextView) view.findViewById(R.id.link);
            counter = (TextView) view.findViewById(R.id.counter);
        }
    }


    public ExamplesAdapter(Context context, List<WordExample> iList) {
        this.wordExampleList = iList;
        _context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_example, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        WordExample wordExample = wordExampleList.get(position);
        String content = wordExample.get_content();
        String query = ((MainActivity) _context)._query;

        Spannable wordtoSpan = new SpannableString(content);
        for (int start = 0; start != -1; ) {
            query = query.toLowerCase();
            content = content.toLowerCase();
            start = content.indexOf(query, start+1);

            if (start != -1) {
                int end = start + query.length();
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.DKGRAY), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else
                break;
        }

        holder.content.setText(wordtoSpan);
        holder.link.setText(wordExample.get_link());
        String strPosition = Integer.toString(position+1);
        strPosition = "#"+strPosition;
        holder.counter.setText(strPosition);
    }

    @Override
    public int getItemCount() {
        return wordExampleList.size();
    }
}
