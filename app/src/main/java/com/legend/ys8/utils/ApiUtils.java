package com.legend.ys8.utils;

import com.legend.ys8.conf.YsApplication;
import com.legend.ys8.model.ImageImpl;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * Created by legend on 2017/9/16.
 */

public class ApiUtils {

    protected static final String URL="http://www.legic.xyz:10001/";

    //


    /**
     * 网络请求 记得开线程，这是同步方法
     * @param doing 传入doing
     * @return 返回json
     */
    public static String getJson(String doing){
        String url=URL+doing;
        Request.Builder builder=new Request.Builder().url(url).method("GET",null);

        Request request=builder.build();

        File sdcard= YsApplication.getContext().getExternalCacheDir();



        int cacheSize=100*1024*1024;

        OkHttpClient.Builder builder1= null;
            builder1 = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20,TimeUnit.SECONDS);

        if (sdcard!=null){
            builder1.cache(new Cache(sdcard.getAbsoluteFile(),cacheSize));
        }

        OkHttpClient okHttpClient=builder1.build();

        Call call=okHttpClient.newCall(request);

        Response response= null;
        String json="";

        try {
            response = call.execute();
            json=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}
