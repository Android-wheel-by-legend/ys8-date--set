package com.legend.ys8.adaper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    int width=0;
    int height=0;


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

        width=YsApplication.getContext().getResources().getDisplayMetrics().widthPixels;

        height= (int) (0.4*width);

        ViewGroup.LayoutParams layoutParams= viewHolder.view.getLayoutParams();

        layoutParams.height=height;

        viewHolder.view.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams layoutParams1= (LinearLayout.LayoutParams) viewHolder.main_item_image.getLayoutParams();

        layoutParams1.height=height;
        layoutParams1.width=height;

        viewHolder.main_item_image.setLayoutParams(layoutParams1);


        viewHolder.view.setOnClickListener(event->
                EventBus.getDefault().post(new MainEvent(viewHolder.main_item_text.getText().toString()))
        );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        super.onBindViewHolder(holder, position);
        String string=strings.get(position);
        holder.main_item_text.setText(string);

        if (string.startsWith("人物")){
            imageLoader.setImage(R.drawable.character,holder.main_item_image,height,height);
        }else if (string.startsWith("世界")){
            imageLoader.setImage(R.drawable.world,holder.main_item_image,height,height);
        }else if (string.startsWith("音乐")){
            imageLoader.setImage(R.drawable.music,holder.main_item_image,height,height);
        }else if (string.startsWith("视频")){
            imageLoader.setImage(R.drawable.movie,holder.main_item_image,height,height);
        }

//        height=YsApplication.getContext().getResources().getDimensionPixelSize(R.dimen.main_height);

//        switch (string){
//            case "人物":
//                imageLoader.setImage(R.drawable.character,holder.main_item_image,height,height);
//                break;
//            case "世界":
//                imageLoader.setImage(R.drawable.world,holder.main_item_image,height,height);
//                break;
//            case "音乐":
//                imageLoader.setImage(R.drawable.music,holder.main_item_image,height,height);
//                break;
//            case "视频":
//                imageLoader.setImage(R.drawable.movie,holder.main_item_image,height,height);
//                break;
//        }

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
