package com.legend.ys8.adaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legend.ys8.R;
import com.legend.ys8.model.CharacterImpl;


import java.util.ArrayList;

/**
 *
 * Created by legend on 2017/8/24.
 */

public class CharacterAdapter extends BaseAdapter<CharacterAdapter.ViewHolder> {


    private ArrayList<CharacterImpl> characters;

    public CharacterAdapter(ArrayList<CharacterImpl> characters) {
        this.characters = characters;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.character_list,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//点击事件

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);


    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    static class ViewHolder extends BaseAdapter.ViewHolder{

        View view;
        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            this.textView=itemView.findViewById(R.id.character_name);
            this.imageView=itemView.findViewById(R.id.character_album);
        }
    }
}
