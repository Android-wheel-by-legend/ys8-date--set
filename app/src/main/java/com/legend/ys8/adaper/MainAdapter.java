package com.legend.ys8.adaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.legend.ys8.R;
import com.legend.ys8.conf.YsApplication;
import com.legend.ys8.event.MainEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 主界面adapter
 * Created by legend on 2017/8/8.
 */

public class MainAdapter extends BaseAdapter<MainAdapter.ViewHolder>{


    ArrayList<String> strings;

    public MainAdapter() {
    }

    public MainAdapter(ArrayList<String> strings) {
        this.strings = strings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.main_list,parent,false);

        final ViewHolder viewHolder=new ViewHolder(view);


        viewHolder.view.setOnClickListener(event-> {
                EventBus.getDefault().post(new MainEvent(viewHolder.main_item_text.getText().toString()));
        }

        );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        super.onBindViewHolder(holder, position);
        String string=strings.get(position);
        holder.main_item_text.setText(string);


    }

    @Override
    public int getItemCount() {

        if (this.strings!=null){
            return this.strings.size();
        }
        return 0;


    }

    static class ViewHolder extends BaseAdapter.ViewHolder{
        View view;
        ImageView main_item_image;
        TextView main_item_text;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            this.main_item_image=view.findViewById(R.id.main_item_image);
            this.main_item_text=view.findViewById(R.id.main_item_text);
        }
    }
}
