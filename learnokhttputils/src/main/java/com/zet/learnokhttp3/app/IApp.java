package com.zet.learnokhttp3.app;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * application
 * 全局类
 */

public class IApp extends Application {

    /**
     * 创建
     */
    @Override
    public void onCreate() {
        super.onCreate();

        initOkHttpUtils();
    }

    /**
     * 初始化
     * okhttputils
     */
    private void initOkHttpUtils() {
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .addInterceptor(new LoggerInterceptor("okhttputils")) // 日志拦截器 TAG
                .build();

        OkHttpUtils.initClient(okHttpClient); // 初始化
    }
}
