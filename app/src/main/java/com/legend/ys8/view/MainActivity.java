package com.legend.ys8.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;


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

    DrawerLayout drawerLayout;

    NavigationView navigationView;





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
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer);
        navigationView= (NavigationView) findViewById(R.id.navigation_header_container);
    }


    /**
     * 捷获取取view
     */
//    private View find(int id){
//        return findViewById(id);
//    }

    //toolbar设置
    private void toolbarConf(){
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                item.setCheckable(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });


        LinearLayout linearLayout= (LinearLayout) navigationView.getHeaderView(0);
        ImageView imageView= linearLayout.findViewById(R.id.image_tou);


        int h=getResources().getDimensionPixelSize(R.dimen.tou_size);

        imageLoader.setImage(R.drawable.tou,imageView,h,h);

        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
    }

    //监听toolbar的Menu按钮点击事件弹出侧滑菜单
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        return super.onOptionsItemSelected(item);
//
//        switch (item.getItemId()){
//            case R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//
//    }

    /**
     * recyclerView设置
     */
    private void recyclerConf(){
        if (strings==null){
            strings=new ArrayList<>();
            strings.add(getResources().getString(R.string.character));
            strings.add(getResources().getString(R.string.world));
            strings.add(getResources().getString(R.string.music));
            strings.add(getResources().getString(R.string.movie));

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

        if (item.startsWith("人物")){
            Intent intent=new Intent(MainActivity.this,CharacterActivity.class);
            startActivity(intent);

        }else if (item.startsWith("世界")){

        }else if (item.startsWith("音乐")){
            Intent intent=new Intent(MainActivity.this,MusicActivity.class);

            startActivity(intent);


        }else if (item.startsWith("视频")){

        }

    }



}
