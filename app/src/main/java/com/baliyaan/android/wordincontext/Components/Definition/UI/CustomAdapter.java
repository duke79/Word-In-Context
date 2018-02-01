package com.baliyaan.android.wordincontext.Components.Definition.UI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baliyaan.android.wordincontext.Model.Definition;
import com.baliyaan.android.wordincontext.R;

import java.util.ArrayList;

/**
 * Created by Pulkit Singh on 1/29/2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<Definition> _definitions;

    public CustomAdapter(ArrayList<Definition> definitions){
        _definitions = definitions;

        /*if (null != view()) {
            if (_definitions.size() < 1) {
                ((CustomView) view()).addDefinition(navigator().getContext().getString(R.string.no_defintion));
            } else {
                ((CustomView) view()).addDefinition(definition._definition);
            }
            view().setVisibility(View.VISIBLE);
        }*/
        
        /*ViewGroup view = (ViewGroup) _inflater.inflate(R.layout.definition_main,this);
        TextView def_tv = (TextView) ((ViewGroup)view.getChildAt(getChildCount()-1)).getChildAt(2);
        def_tv.setText(definition);

        //_adapter.notifyDataSetChanged(); //Todo*/
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.definition_main,parent,false);
        if(null != view) {
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setDefinition(_definitions.get(position)._definition);
    }

    @Override
    public int getItemCount() {
        return _definitions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView _defTV;
        private View _view;
        public ViewHolder(View itemView) {
            super(itemView);
            _view = itemView;
            _defTV = (TextView) _view.findViewById(R.id.definition_tv);
        }

        public void setDefinition(String definition){
            if (null != _defTV) {
                _defTV.setText(definition);
            }
        }
    }
}
