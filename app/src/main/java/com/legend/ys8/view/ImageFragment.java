package com.legend.ys8.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.legend.ys8.R;
import com.legend.ys8.conf.YsApplication;

/**
 * 加载角色图片，需传入URL
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends BaseFragment {


    String url;
    ImageView imageView;
    int width=0;
    int height=0;


    public void setUrl(String url) {
        this.url = url;
    }

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(){

        return new ImageFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_image, container, false);
        imageView=view.findViewById(R.id.image);
        imageConf();
        setImage();
        return view;
    }

    private void imageConf(){

        int w= YsApplication.getContext().getResources().getDisplayMetrics().widthPixels;

        int h= (int) (0.88888888*w);

        FrameLayout.LayoutParams layoutParams= (FrameLayout.LayoutParams) imageView.getLayoutParams();

        layoutParams.height=h;
        this.width=w;
        this.height=h;

        imageView.setLayoutParams(layoutParams);
    }

    private void setImage(){
        imageHelper.setImage(url,imageView,this.width,this.height);
    }

}
