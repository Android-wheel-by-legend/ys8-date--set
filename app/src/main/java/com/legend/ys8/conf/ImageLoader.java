package com.legend.ys8.conf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.functions.Consumer;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * Created by legend on 2017/9/13.
 */

public class ImageLoader{

    private static final int CPU_COUNT=Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE=CPU_COUNT+1;
    private static final int MAXNUM_POOL_SIZE=CPU_COUNT*2+1;
    private static final long KEEP_ALIVE=10L;

    private LruCache<String,Bitmap> lruCache;

    private static ImageLoader imageLoader;

    private final String CACHE_PATH= String.valueOf(YsApplication.getContext().getFilesDir());//文件缓存


    private RecyclerView recyclerView;

    public ImageLoader() {
        int maxMemory= (int) (Runtime.getRuntime().maxMemory()/1024);
        int cacheSize=maxMemory/8;

        lruCache=new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {

                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };

    }


    //单例模式
    public static ImageLoader getImageLoader(){
        if (imageLoader==null){
            imageLoader=new ImageLoader();
        }

        return imageLoader;
    }



    private static final ThreadFactory mThreadFactory=new ThreadFactory() {
        private final AtomicInteger count=new AtomicInteger(1);
        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable,"ImageLoader#"+count.getAndIncrement());
        }
    };


    private static final Executor ThreadPool=new ThreadPoolExecutor(
            CORE_POOL_SIZE,MAXNUM_POOL_SIZE,KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(),mThreadFactory);

   //设置image
    public void setImage(String url, ImageView imageView,int reqWidth,int reqHeight) {

        bindImage(url, imageView,reqWidth,reqHeight);

    }



    boolean isScroll=false;

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    //清除缓存
    public void clean(){
        lruCache.evictAll();

        File file=new File(CACHE_PATH);

        if (file!=null&&file.exists()&&file.isDirectory()){

            for (File file1:file.listFiles()){
                file1.delete();
            }
        }

    }


    /**
     * 根据URL获取网络图片
     * @param url
     * @return
     */
    private Bitmap getBitmap(String url,int reqWidth,int reqHeight){


        Request.Builder builder1=new Request.Builder().url(url).method("GET",null);

        Request request=builder1.build();

        OkHttpClient.Builder builder=new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS);

        OkHttpClient okHttpClient=builder.build();

        Call call=okHttpClient.newCall(request);

        InputStream inputStream=null;

        Bitmap bitmap=null;

        try {
            Response response=call.execute();
            inputStream=response.body().byteStream();



//            bitmap=writeToDisk(inputStream,url,reqWidth,reqHeight);

            BitmapFactory.Options options=new BitmapFactory.Options();

            options.inPreferredConfig= Bitmap.Config.RGB_565;


            options.inSampleSize=2;

            bitmap=BitmapFactory.decodeStream(inputStream,null,options);


            Log.d("internetSize--------->",bitmap.getByteCount()+"");


        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
//        return Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,false);

    }

    //写入本地&返回Bitmap
    private Bitmap writeToDisk(InputStream inputStream,String url,int reqWidth,int reqHeight){

        if (inputStream==null){
            return null;
        }

        Bitmap bitmap=null;

        File filePath=new File(CACHE_PATH).getAbsoluteFile();

        if (!filePath.exists()){
            filePath.mkdirs();
        }

        FileOutputStream fileOutputStream=null;

        String name=getMd5(url);

        File image=new File(filePath,name);

        String path=filePath+"/"+name;

        if (null!=image){
            try {
                fileOutputStream=new FileOutputStream(image);

                byte[] buffer=new byte[2048];

                int len=0;

                while ((len=inputStream.read(buffer))!=-1){
                    fileOutputStream.write(buffer,0,len);
                }

                fileOutputStream.flush();

                bitmap=BitmapFactory.decodeFile(path);

                if (bitmap!=null){
                    bitmap=Bitmap.createScaledBitmap(bitmap,reqWidth,reqHeight,false);
                }



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    //真·设置image的地方
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {


//            super.handleMessage(msg);
            LoaderResult loaderResult= (LoaderResult) msg.obj;

            String url=loaderResult.url;


            ImageView imageView=loaderResult.imageView;

            Bitmap bitmap=loaderResult.bitmap;

//            Bitmap bitmap=Bitmap.createScaledBitmap(b,b.getWidth()/2,b.getHeight()/2,false);


            if (null!=bitmap){

                bitmap=Bitmap.createScaledBitmap(bitmap,loaderResult.width,loaderResult.height,false);

                imageView.setImageBitmap(bitmap);

                //双重缓存
                cacheInMemory(bitmap,url);
                cacheImageInDisk(bitmap,url);

            }
        }
    };

    private static class LoaderResult{
        private ImageView imageView;
        private Bitmap bitmap;
        private String url;
        int width,height;


        public LoaderResult(ImageView imageView, Bitmap bitmap, String url,int reqWidth,int reqHeight) {
            this.imageView = imageView;
            this.bitmap = bitmap;
            this.url = url;
            this.width=reqWidth;
            this.height=reqHeight;

        }
    }

    //开线程执行放置image
    private void bindImage(String url,ImageView imageView,int reqWidth,int reqHeight){





        Log.d("isScroll",isScroll+"");



        //先查找内存缓存以及本地缓存，如果有则设置并返回，没有则开启网络查找
        Bitmap bitmap=null;

        bitmap=getBitmapFromMemory(url,reqWidth,reqHeight);

        if (null!=bitmap){

            imageView.setImageBitmap(bitmap);
            return;
        }

        if (isScroll){
            return;
        }

        bitmap=getBitmapFromDisk(url,reqWidth,reqHeight);

        if (null!=bitmap){

            Log.d("size------->",(bitmap.getByteCount())+"");

            imageView.setImageBitmap(bitmap);
            return;
        }





        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap=getBitmap(url,reqWidth,reqHeight);

                if (null!=bitmap){

                    LoaderResult loaderResult=new LoaderResult(imageView,bitmap,url,reqWidth,reqHeight);
                    handler.obtainMessage(1,loaderResult).sendToTarget();

                }
            }
        };

        ThreadPool.execute(runnable);//放入线程池

    }

    /**
     * 本地缓存
     * @param bitmap
     * @param url MD5加密命名
     */
    private void cacheImageInDisk(Bitmap bitmap,String url){
        String name=getMd5(url);

        try {


            File file=new File(CACHE_PATH,name);



            File parentFile=file.getParentFile();

            if (!parentFile.exists()){
                parentFile.mkdirs();
            }

            bitmap.compress(Bitmap.CompressFormat.WEBP,100,new FileOutputStream(file));

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }

    /**
     * 从本地读取缓存
     * @param url
     * @return
     */
    private Bitmap getBitmapFromDisk(String url,int reqWidth,int reqHeight){
        Bitmap bitmap=null;
        String name=getMd5(url);

        File file=new File(CACHE_PATH).getAbsoluteFile();

        String file_path=file+"/"+name;

        BitmapFactory.Options options=new BitmapFactory.Options();

        options.inSampleSize=2;
        options.inPreferredConfig= Bitmap.Config.RGB_565;


        bitmap=BitmapFactory.decodeFile(file_path,options);


//        try {
//            FileInputStream fileInputStream=new FileInputStream(file);
//            bitmap=BitmapFactory.decodeStream(fileInputStream);
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//
//
//        }finally {
            if (null!=bitmap) {
                bitmap=Bitmap.createScaledBitmap(bitmap,reqWidth,reqHeight,false);

                cacheInMemory(bitmap, url);
            }

            return bitmap;
//        }

        //还需要进行内存缓存


//        return bitmap;

    }

    /**
     * 写入内存缓存
     * @param bitmap
     * @param url
     */
    private void cacheInMemory(Bitmap bitmap,String url){
        String name=getMd5(url);

        lruCache.put(name,bitmap);
    }

    /**
     * 读取内存缓存
     * @param url
     * @return
     */
    private Bitmap getBitmapFromMemory(String url,int reqWidth,int reqHeight){
        String name=getMd5(url);

//        System.out.println("url--------->"+url);

        Bitmap bitmap=lruCache.get(name);

        if (bitmap!=null) {

            bitmap = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, false);
        }

        return bitmap;
    }



    //md5加密改名
    private String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    private Bitmap decodeBitmap(InputStream inputStream,int reqWidth,int reqHeight){

        try {
            inputStream.mark(inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BitmapFactory.Options options=new BitmapFactory.Options();



        options.inJustDecodeBounds=true;



        Bitmap bitmap=BitmapFactory.decodeStream(inputStream,null,options);



        options.inSampleSize=reSize(options,reqWidth,reqHeight);


        options.inJustDecodeBounds=false;

        try {
            inputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bitmap=BitmapFactory.decodeStream(inputStream,null,options);


        return bitmap;

    }

    private int reSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        int size=1;

        int width=options.outWidth;

        int height=options.outHeight;



        if (height>reqHeight/2||width>reqWidth/2){
            int halfHeight=height/2;

            int halfWidth=width/2;

            while ((halfHeight/size)>=reqHeight&&
                    (halfWidth/size)>=reqWidth){
                size *=2;
            }
        }

        return size;
    }




}
