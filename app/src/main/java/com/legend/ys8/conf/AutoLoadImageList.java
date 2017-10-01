package com.legend.ys8.conf;

import android.content.Context;
import android.support.annotation.Nullable;

import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


/**
 *
 * Created by legend on 2017/9/29.
 */

public class AutoLoadImageList extends RecyclerView{
    public AutoLoadImageList(Context context) {
        super(context);
    }

    public AutoLoadImageList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadImageList(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }





    class AutoLoaderListener extends OnScrollListener{
        public AutoLoaderListener() {
            super();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);


        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }


}
