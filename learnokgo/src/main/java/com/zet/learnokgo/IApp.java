package com.zet.learnokgo;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created by Zet on 2017/6/26.
 */

public class IApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initOkGo();
    }

    private void initOkGo() {
        // 1. 构建OkHttpClient.Builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // 2. 配置log  日志拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor("OkGo");
        httpLoggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY); // log打印级别
        httpLoggingInterceptor.setColorLevel(Level.INFO); // log颜色级别
        builder.addInterceptor(httpLoggingInterceptor); // builder添加日志拦截器

        // 3. 配置超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS); // 读取超时
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS); // 连接超时
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS); // 写入超时

        // 4. 配置cookie
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this))); // sp cookie

        // 5. Https配置   信任所有
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);

        // 6. 配置OKGO
        OkGo.getInstance()
                .init(this)
                .setOkHttpClient(builder.build());
    }
}
