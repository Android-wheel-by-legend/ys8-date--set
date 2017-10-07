package com.legend.ys8.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.legend.ys8.R;
import com.legend.ys8.utils.ApiUtils;

public class MusicActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        test();
    }

    private void test(){

        new Thread() {
            @Override
            public void run() {
                super.run();
                String doing = "get-music-list";

                String json = ApiUtils.getJson(doing);

                Log.d("json------>",json);
            }
        }.start();
    }
}
