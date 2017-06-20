package com.zet.learnokhttp3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 主活动Activity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 静态常量TAG
     */
    private static final String TAG = "MainActivity";

    private static String baseUrl = "http://192.168.137.1:8080/web/";

    private TextView mTVInfo;
    private Button mBtnGet_1;
    private Button mBtnGet_2;

    /**
     * 创建
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            initView();
        }
    }

    private void initView() {
        mTVInfo = (TextView) findViewById(R.id.mTVInfo);
        mBtnGet_1 = (Button) findViewById(R.id.mBtnGet_1);
        mBtnGet_2 = (Button) findViewById(R.id.mBtnGet_2);

        mBtnGet_1.setOnClickListener(this);
        mBtnGet_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtnGet_1:
                getHtml();
                break;
            case R.id.mBtnGet_2:
                getGitHubHtml();
                break;
        }
    }

    private void getHtml() {
        String url = baseUrl + "User_login";
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

    private void getGitHubHtml() {
        String url = "http://www.piaotian.com/";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: " + response);
                        mTVInfo.setText(response);
                    }
                });
    }

}
