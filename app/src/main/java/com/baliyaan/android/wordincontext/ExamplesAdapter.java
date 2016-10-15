package com.baliyaan.android.wordincontext;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pulkit Singh on 10/15/2016.
 */

public class ExamplesAdapter extends RecyclerView.Adapter<ExamplesAdapter.MyViewHolder>{
    private List<WordExample> wordExampleList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView content, link;

        public MyViewHolder(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.content);
            link = (TextView) view.findViewById(R.id.link);
        }
    }


    public ExamplesAdapter(List<WordExample> iList) {
        this.wordExampleList = iList;
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
        holder.content.setText(wordExample.get_content());
        holder.link.setText(wordExample.get_link());
    }

    @Override
    public int getItemCount() {
        return wordExampleList.size();
    }
}
