package com.zet.learnokhttp2.app;

import android.app.Application;
import android.util.Log;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Created by Zet on 2017/6/14.
 */

public class GlobalApplication extends Application
{
    // 创建okhttpclient对象
    public final static OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public void onCreate()
    {
        super.onCreate();

        initOkhttp();


    }

    private void initOkhttp()
    {
        Log.d(TAG, "initOkhttp: " + getExternalCacheDir().getAbsolutePath());
        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        okHttpClient.setCache(new Cache(sdcache.getAbsoluteFile(), cacheSize));

        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS); // TUS
        okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);

    }
}
