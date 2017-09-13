package com.legend.ys8.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.legend.ys8.R;
import com.legend.ys8.adaper.CharacterAdapter;
import com.legend.ys8.conf.CharacterItemDecoration;
import com.legend.ys8.http.Character;
import com.legend.ys8.model.CharacterImpl;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
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
    ArrayList<Character> characters;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
    }

    @Override
    protected void onStart() {
        super.onStart();
        find();
        characterConf();
//        recyclerConf();
        toolbarConf();

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
    }

    /**
     * 找组件
     */
    private void find(){
        recyclerView= (RecyclerView) find(R.id.character_recycler);
        toolbar= (Toolbar) find(R.id.toolbar_character);

    }

    private void characterConf(){

        Request.Builder builder=new Request.Builder().url(url+"get-character-list");

        builder.method("GET",null);

        Request request=builder.build();

        OkHttpClient okHttpClient=new OkHttpClient();

        Call call=okHttpClient.newCall(request);


            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    Response response= null;
                    try {
                        response = call.execute();
                        System.out.println(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };




    }

    private void recyclerConf(){

//        if (linearLayoutManager==null) {
//            linearLayoutManager = new LinearLayoutManager(this);
//            adapter = new CharacterAdapter(characters);
//            recyclerView.setLayoutManager(linearLayoutManager);
//            recyclerView.setAdapter(adapter);
//            recyclerView.addItemDecoration(new CharacterItemDecoration());
//        }
    }

    /**
     *
     */
    private void toolbarConf(){
        toolbar.setTitle(R.string.character);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(ContextCompat.getColor(CharacterActivity.this,R.color.colorPrimary));

        toolbar.setNavigationOnClickListener(event->finish());
    }



}
