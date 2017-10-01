package com.legend.ys8.view;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legend.ys8.R;
import com.legend.ys8.adaper.CharacterImageAdapter;
import com.legend.ys8.conf.YsApplication;
import com.legend.ys8.model.CharacterImpl;
import com.legend.ys8.model.ImageImpl;
import com.legend.ys8.utils.JsonUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CharacterFragment extends BaseFragment {

    Toolbar toolbar;

    ViewPager viewPager;

    TextView textView;

    CharacterImpl character;

    String[] strings;

    ImageImpl image;

    CharacterImageAdapter adapter;

    public void setCharacter(CharacterImpl character) {
        this.character = character;

    }

    public CharacterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_character, container, false);

        toolbar=view.findViewById(R.id.character_info_toolbar);

        viewPager=view.findViewById(R.id.viewPager);

        textView=view.findViewById(R.id.character_info);

        int w= YsApplication.getContext().getResources().getDisplayMetrics().widthPixels;

        int h= (int) (0.88888888*w);

        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) viewPager.getLayoutParams();

        params.height=h;
        viewPager.setLayoutParams(params);

        toolbarConf();//toolbar设置

        if (character!=null){
            getJson(character.getE_name());
        }


        return view;
    }






    private void getJson(String name){

        String url=URL+"get-image-by-name?name="+name;
        Request.Builder builder=new Request.Builder().url(url).method("GET",null);

        Request request=builder.build();

        OkHttpClient.Builder builder1=new OkHttpClient.Builder().retryOnConnectionFailure(true).readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS);

        OkHttpClient okHttpClient=builder1.build();

        Call call=okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json =response.body().string();

                ImageImpl  image= (ImageImpl) JsonUtil.getObject(json,ImageImpl.class);

                handler.obtainMessage(100,image).sendToTarget();

            }
        });





    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            image= (ImageImpl) msg.obj;

            if (character==null){
                return;
            }


            strings=image.getUrl().split(";");

            adapter=new CharacterImageAdapter(getActivity().getSupportFragmentManager());
            viewPager.setAdapter(adapter);

            adapter.setStrings(strings);
//            viewPager.setAdapter(adapter);

            textView.setText(character.getIntroduction());

        }
    };




    private void toolbarConf(){
        toolbar.setTitle(character.getName());
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(event->removeFragment(CharacterFragment.this));


    }

}
