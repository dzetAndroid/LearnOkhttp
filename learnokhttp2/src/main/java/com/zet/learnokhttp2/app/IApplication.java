package com.zet.learnokhttp2.app;

import android.app.Application;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zet on 2017/6/14.
 */

public class IApplication extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();

        initOkhttp();
//        initNoHttp();
    }

//    private void initNoHttp()
//    {
//
//        NoHttp.Config config = new NoHttp.Config()
//                // 设置全局连接超时时间，单位毫秒
//                .setConnectTimeout(30 * 1000)
//                // 设置全局服务器响应超时时间，单位毫秒
//                .setReadTimeout(30 * 1000)
//                .setCacheStore(
//                        // 保存到数据库
//                        new DBCacheStore(this).setEnable(true) // 如果不使用缓存，设置false禁用。
//                        // 或者保存到SD卡：new DiskCacheStore(this)
//                )
//                // 默认保存数据库DBCookieStore，开发者也可以自己实现CookieStore接口。
//                .setCookieStore(
//                        new DBCookieStore(this).setEnable(false) // 如果不维护cookie，设置false禁用。
//                )
//                // 使用HttpURLConnection
////                        .setNetworkExecutor(new URLConnectionNetworkExecutor())
//                // 或者使用OkHttp
//                .setNetworkExecutor(new OkHttpNetworkExecutor());
//
//        NoHttp.initialize(this, config);
//        Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
//        Logger.setTag("NoHttpSample");// 设置NoHttp打印Log的tag。
//
//    }

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
