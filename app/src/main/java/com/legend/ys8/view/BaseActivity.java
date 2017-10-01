package com.legend.ys8.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.legend.ys8.R;
import com.legend.ys8.conf.ImageLoader;

public class BaseActivity extends AppCompatActivity {

    protected static final String url="http://www.legic.xyz:10001/";

    protected ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        imageLoader=ImageLoader.getImageLoader();
    }

    protected View find(int id){
        return findViewById(id);
    }

    protected void openFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.character_detail,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
