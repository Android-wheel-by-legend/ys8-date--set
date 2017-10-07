package com.legend.ys8.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legend.ys8.R;
import com.legend.ys8.adaper.CharacterImageAdapter;
import com.legend.ys8.conf.YsApplication;
import com.legend.ys8.model.CharacterImpl;
import com.legend.ys8.model.ImageImpl;
import com.legend.ys8.utils.ApiUtils;
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

    NestedScrollView nestedScrollView;

    private static final String URL = "get-image-by-name?name=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_info);

        getComponent();
        changeToolbarAlpha();
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
        nestedScrollView= (NestedScrollView) findViewById(R.id.scroll);
    }

    private void toolbarConf() {
        toolbar.setTitle(getResources().getString(R.string.character));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());

        toolbar.setBackgroundColor(Color.parseColor("#00000000"));

    }

    private void pageConf() {
        if (null == adapter) {
            adapter = new CharacterImageAdapter(getSupportFragmentManager());

            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) viewPager.getLayoutParams();
            int h= (int) (YsApplication.getContext().getResources().getDisplayMetrics().widthPixels*0.888888);

            layoutParams.height=h;

            viewPager.setLayoutParams(layoutParams);

            viewPager.setAdapter(adapter);

        }
    }


    //获取数据
    private void getData() {
        Intent intent = getIntent();
        String json=intent.getStringExtra("json");

        CharacterImpl character= (CharacterImpl) JsonUtil.getObject(json,CharacterImpl.class);

        String name = character.getE_name();
        textView.setText(character.getIntroduction());
        toolbar.setTitle(character.getName());

        if (null != name) {
            Observable
                    .create(new ObservableOnSubscribe<String[]>() {
                        @Override
                        public void subscribe(ObservableEmitter<String[]> e) throws Exception {
                            String doing=URL+name;

                            String json=ApiUtils.getJson(doing);

                            ImageImpl image= (ImageImpl) JsonUtil.getObject(json,ImageImpl.class);

                            String[] str=image.getUrl().split(";");

                            e.onNext(str);
                        }
                    })
                    .subscribeOn(Schedulers.io())

                    .observeOn(AndroidSchedulers.mainThread())

                    .subscribe(new Consumer<String[]>() {
                        @Override
                        public void accept(String[] strings) throws Exception {
                            if (strings!=null){
                                adapter.setStrings(strings);
                            }
                        }
                    });
        }
    }

    private void changeToolbarAlpha(){
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int h= (int) (YsApplication.getContext().getResources().getDisplayMetrics().widthPixels*0.888888);

                int alpha=0;

                 alpha=scrollY/(h/255);

                 if (alpha>255){
                     alpha=255;
                 }

                 if (alpha<0){
                     alpha=0;
                 }

                String col="5ac0ff";

                String hex=Integer.toHexString(alpha);

                if (hex.length()<2){//保证单位数的时候不会无法转换成相应颜色
                    hex=String.valueOf("0"+""+hex);
                }

                String color="#"+hex+col;

                toolbar.setBackgroundColor(Color.parseColor(color));

            }
        });
    }


}
