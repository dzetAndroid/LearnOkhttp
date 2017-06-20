package com.zet.learnokhttp3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 主活动Activity
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 静态常量TAG
     */
    private static final String TAG = "MainActivity";

    /**
     * 创建
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private static String baseUrl = "http://192.168.137.1:8080/web/";

    private void getHtml() {
        String url = baseUrl + "login";

        OkHttpUtils
                .get()
                .url(url)
                .addParams("username", "user_1")
                .addParams("password", "pass_1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: " + response);
                    }
                });
    }
}
