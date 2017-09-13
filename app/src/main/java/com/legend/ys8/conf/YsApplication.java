package com.legend.ys8.conf;

import android.app.Application;
import android.content.Context;

/**
 *
 * Created by legend on 2017/8/22.
 */

public class YsApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
