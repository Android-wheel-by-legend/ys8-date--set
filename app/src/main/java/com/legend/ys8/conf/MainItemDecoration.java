package com.legend.ys8.conf;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.legend.ys8.R;

/**
 *
 * Created by legend on 2017/8/24.
 */

public class MainItemDecoration extends RecyclerView.ItemDecoration{

    public MainItemDecoration() {
        super();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int default_space= view.getResources().getDimensionPixelSize(R.dimen.main_default_space);
        int bottom=view.getResources().getDimensionPixelSize(R.dimen.mian_item_bottom);
        int top=view.getResources().getDimensionPixelSize(R.dimen.main_item_top);



        if (parent.getChildAdapterPosition(view)==0){
            outRect.top=top;
        }

        outRect.bottom=bottom;

    }
}
