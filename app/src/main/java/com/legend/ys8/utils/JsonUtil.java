package com.legend.ys8.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.legend.ys8.model.CharacterImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析json数据
 * Created by legend on 2017/9/13.
 */

public class JsonUtil {

    /**
     * 将json转为相对应的对象数组并返回
     * @param json
     * @return
     */
    public static List getJsonList(String json,Class<?> m){

        List list=new ArrayList();
        Gson gson=new GsonBuilder().create();
        try {
            JSONArray jsonArray=new JSONArray(json);
            for (int i=0;i<jsonArray.length();i++){

                Object object=gson.fromJson(String.valueOf(jsonArray.get(i)),m);

                list.add(object);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    //获取单个对象
    public static Object getObject(String json,Class<?> t){

        Gson gson=new GsonBuilder().create();

        Object object=gson.fromJson(json,t);

        return object;

    }

    public static String toJson(Object o){

        String json=new Gson().toJson(o);

        return json;
    }






}
