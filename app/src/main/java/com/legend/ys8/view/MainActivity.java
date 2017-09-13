package com.legend.ys8.view;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


import com.legend.ys8.R;
import com.legend.ys8.adaper.MainAdapter;
import com.legend.ys8.conf.MainItemDecoration;
import com.legend.ys8.event.MainEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;



import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.Response;

public class MainActivity extends BaseActivity {

    /**
     *定义各变量
     *
     */
    Toolbar toolbar;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MainAdapter adapter;
    ArrayList<String> strings;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 准备数据
     */
    @Override
    protected void onStart() {
        super.onStart();
        getViewById();
        toolbarConf();
        recyclerConf();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }


    /**
     * 恢复数据
     */
    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * 销毁并取消注册
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取view组件
     */
    private void getViewById(){
        toolbar= (Toolbar) find(R.id.main_toolbar);
        recyclerView= (RecyclerView) find(R.id.recycler_view);
    }


    /**
     * 捷获取取view
     */
//    private View find(int id){
//        return findViewById(id);
//    }

    //toolbar设置
    private void toolbarConf(){
        if (toolbar!=null){
            System.out.println("toolbar is "+toolbar);
            toolbar.setTitle("测试数据！");
            toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorBlue));

        }
    }

    /**
     * recyclerView设置
     */
    private void recyclerConf(){
        if (strings==null){
            strings=new ArrayList<>();
            strings.add("人物");
            strings.add("世界观");
            strings.add("音乐");
            strings.add("视频");

            adapter=new MainAdapter(strings);
            linearLayoutManager=new LinearLayoutManager(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addItemDecoration(new MainItemDecoration());
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)

    public void mainEvent(MainEvent mainEvent){
        String item=mainEvent.getItem();
        switch (item){
            case "人物":
                Intent intent=new Intent(MainActivity.this,CharacterActivity.class);
                startActivity(intent);
                break;
            case "音乐":
                getMusic();
                break;


        }
    }


    private void getMusic(){
        Request.Builder builder=new Request.Builder().url("https://api.imjad.cn/cloudmusic/?type=song&id=426881983&br=320000");

//        RequestBody formBody=new FormBody.Builder().add("type","song").add("id","426881983").build();
//
//        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.imjad.cn/cloudmusic").newBuilder();
//        urlBuilder.addQueryParameter("type", "song");
//        urlBuilder.addQueryParameter("id","426881983");
//        builder.url(urlBuilder.build());


        builder.method("GET",null);

        Request request=builder.build();

        OkHttpClient okHttpClient=new OkHttpClient();

        Call call=okHttpClient.newCall(request);


        System.out.println(request);




        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("false------"+call);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String title=response.body().string();
                System.out.println("call is "+call);

                System.out.println(title);
            }
        });

    }


}
