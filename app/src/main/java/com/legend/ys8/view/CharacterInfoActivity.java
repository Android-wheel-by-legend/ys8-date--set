package com.legend.ys8.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.legend.ys8.R;
import com.legend.ys8.adaper.CharacterImageAdapter;
import com.legend.ys8.conf.YsApplication;
import com.legend.ys8.model.CharacterImpl;
import com.legend.ys8.utils.JsonUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CharacterInfoActivity extends BaseActivity {

    ViewPager viewPager;

    TextView textView;

    Toolbar toolbar;
    CharacterImageAdapter adapter;

    private static final String URL = "get-image-by-name?name=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_info);

        getComponent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        toolbarConf();
        pageConf();
        getData();
    }

    private void getComponent() {

        toolbar = (Toolbar) findViewById(R.id.info_toolbar);
        textView = (TextView) findViewById(R.id.character_info_string);
        viewPager = (ViewPager) findViewById(R.id.character_viewPager);
    }

    private void toolbarConf() {
        toolbar.setTitle(getResources().getString(R.string.character));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());

    }

    private void pageConf() {
        if (null == adapter) {
            adapter = new CharacterImageAdapter(getSupportFragmentManager());

            viewPager.setAdapter(adapter);

        }
    }


    //获取数据
    private void getData() {
        Intent intent = getIntent();
        String json=intent.getStringExtra("json");

        CharacterImpl character= (CharacterImpl) JsonUtil.getObject(json,CharacterImpl.class);

        String name = character.getE_name();

        if (null != name) {
            Observable
                    .create(new ObservableOnSubscribe<String>() {

                        @Override
                        public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                            for (int i=0;i<5;i++) {
                                e.onNext("test");
                            }
                        }
                    })
                    .subscribeOn(Schedulers.io())

                    .observeOn(AndroidSchedulers.mainThread())

                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            Log.d("s--->",s);
                        }
                    });

        }

        String doing = URL;

    }


}
