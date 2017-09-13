package com.legend.ys8.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.legend.ys8.R;

public class BaseActivity extends AppCompatActivity {

    protected static final String url="http://www.legic.xyz:10001/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected View find(int id){
        return findViewById(id);
    }
}
