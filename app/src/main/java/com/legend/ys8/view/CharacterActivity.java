package com.legend.ys8.view;


import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.legend.ys8.R;
import com.legend.ys8.adaper.CharacterAdapter;
import com.legend.ys8.conf.CharacterItemDecoration;

import com.legend.ys8.event.CharacterInfoEvent;
import com.legend.ys8.model.CharacterImpl;
import com.legend.ys8.utils.ApiUtils;
import com.legend.ys8.utils.JsonUtil;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 角色列表界面
 */
public class CharacterActivity extends BaseActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    CharacterAdapter adapter;
    ArrayList<CharacterImpl> characters;
    Toolbar toolbar;

    SwipeRefreshLayout swipeRefreshLayout;

    private static final String URL="get-character-list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        find();
        recyclerConf();
        getData();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        characterConf();
//        recyclerConf();
        toolbarConf();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 找组件
     */
    private void find(){
        recyclerView= (RecyclerView) find(R.id.character_recycler);
        toolbar= (Toolbar) find(R.id.toolbar_character);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.refresh);

    }



    private void characterConf(){

        if (null==characters) {

            File sdcard = getExternalCacheDir();
            int catchSize = 10 * 1024 * 1024;

            Request.Builder builder = new Request.Builder().url(url + "get-character-list");

            builder.method("GET", null);

            Request request = builder.build();
            OkHttpClient.Builder builder1 = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .cache(new Cache(sdcard.getAbsoluteFile(), catchSize));

            OkHttpClient okHttpClient = builder1.build();

            Call call = okHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    characters = (ArrayList<CharacterImpl>) JsonUtil.getJsonList(json, CharacterImpl.class);
                    handler.sendEmptyMessage(100);
                }
            });


        }
    }

   Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            int i=msg.what;
            if (i==100){
                recyclerConf();
            }

        }
    };


    //使用Rxjava异步获取数据并展示
    private void getData(){
        Observable
                .create(new ObservableOnSubscribe<Integer>() {

                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                        String doing=URL;

                        String json= ApiUtils.getJson(URL);

                        Log.d("json----->",json);

                        characters = (ArrayList<CharacterImpl>) JsonUtil.getJsonList(json, CharacterImpl.class);

                        e.onNext(100);
                        e.onComplete();


                    }
                })
                .subscribeOn(Schedulers.io())//io线程执行网络任务
                .observeOn(AndroidSchedulers.mainThread())//主线程更改UI
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer.intValue()==100){
//                            recyclerConf();
                            setData();
                        }
                    }
                });
    }



    private void recyclerConf(){

        if (linearLayoutManager==null) {
            linearLayoutManager = new LinearLayoutManager(this);
            adapter = new CharacterAdapter();
            adapter.setRecyclerView(recyclerView);

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new CharacterItemDecoration());



            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    imageLoader.clean();

                    getData();//重新获取数据

                    swipeRefreshLayout.setRefreshing(false);

                }
            });

        }
    }

    /**
     *
     */
    private void toolbarConf(){
        toolbar.setTitle(R.string.character);

        setSupportActionBar(toolbar);
//        toolbar.setTitle("角色");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(event->finish());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openCharacter(CharacterInfoEvent event){
//        System.out.println(event.getCharacter());
//        CharacterFragment characterFragment=new CharacterFragment();
//        characterFragment.setCharacter(event.getCharacter());
//        openFragment(characterFragment);

        CharacterImpl character=event.getCharacter();

        Intent intent=new Intent(CharacterActivity.this,CharacterInfoActivity.class);
        String json=JsonUtil.toJson(character);

        intent.putExtra("json",json);

        startActivity(intent);

//        Log.d("json",JsonUtil.toJson(event.getCharacter()));

    }

    private void setData(){
        if (null!=characters){
            adapter.setCharacters(characters);
        }
    }





}
