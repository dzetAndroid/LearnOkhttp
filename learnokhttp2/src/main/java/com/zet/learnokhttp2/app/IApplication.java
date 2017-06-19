package com.zet.learnokhttp2.app;

import android.app.Application;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * application
 */

public class IApplication extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();

        initOkhttp();
    }

    // 创建okhttpclient对象
    public final static OkHttpClient okHttpClient = new OkHttpClient();

    private void initOkhttp()
    {
        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        okHttpClient.setCache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS); // TUS
        okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);

    }

}
