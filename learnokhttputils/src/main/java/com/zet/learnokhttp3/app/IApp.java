package com.zet.learnokhttp3.app;

import android.app.Application;

import com.google.gson.Gson;
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

    private static final Gson GSON = new Gson();

    public static Gson getGson() {
        return GSON;
    }

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
                .connectTimeout(10000L, TimeUnit.MILLISECONDS) // 连接超时
                .readTimeout(10000L, TimeUnit.MILLISECONDS) // 读取超时
                .cookieJar(cookieJar) // cookie
                .addInterceptor(new LoggerInterceptor("okhttputils")) // 日志拦截器 TAG
                .build(); // 构造

        OkHttpUtils.initClient(okHttpClient); // 初始化 [ 构造后的okhttpclient ]
    }
}
