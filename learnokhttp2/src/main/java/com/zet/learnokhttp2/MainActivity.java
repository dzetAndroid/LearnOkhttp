package com.zet.learnokhttp2;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.zet.learnokhttp2.app.IApplication;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.zet.learnokhttp2.global.IField.mBasePostFileUrl;

/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "MainActivity";
    private Button mBtnPostFileAsyncHttpClient;
    private Button mBtnPostFileOkHttp2;
    private TextView mTvResult;
    private ProgressBar mPbProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        getPermission();

        postFileOkHttp2();
    }

    /**
     * 获取文件读写权限
     */
    private void getPermission()
    {
        AndPermission
                .with(getApplicationContext()) // activity fragment context
                .requestCode(200) // requestCode
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(new PermissionListener()
                {
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions)
                    {
                        if (requestCode == 200)
                        {
                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions)
                    {
                        if (requestCode == 200)
                        {
                            System.exit(0);
                        }
                    }
                }) // callback
                .start(); // start
    }

    /**
     * findviewbyid
     */
    private void initView()
    {
        mTvResult = (TextView) findViewById(R.id.mTvResult);
        mTvResult.setOnClickListener(this);
        mPbProgress = (ProgressBar) findViewById(R.id.mPbProgress);
        mPbProgress.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.mBtnPostFileAsyncHttpClient:
                postFileAsyncHttpClient();
                break;
            case R.id.mBtnPostFileOkHttp2:
                postFileOkHttp2();
                break;
        }
    }

    private void postFileOkHttp2()
    {
        try
        {
            String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
            String fileName = "cc.jpg";
            File file = new File(canonicalPath, fileName);
            Log.e(TAG, "file.length(): " + file.length());
            Log.e(TAG, "file.getAbsolutePath(): " + file.getAbsolutePath());
            if (!file.exists())
            {
                return;
            }

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

            Request request = new Request.Builder()
                    .url(mBasePostFileUrl)
                    .post(requestBody)
                    .build();

            Call call = IApplication.okHttpClient.newCall(request);

            call.enqueue(new Callback()
            {
                @Override
                public void onFailure(Request request, IOException e)
                {

                }

                @Override
                public void onResponse(Response response) throws IOException
                {
                    String string = response.body().string();
                    Log.e(TAG, "onResponse: " + string);
                }
            });

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     * 小文件还行
     * 大文件, 绝对要死, 总是超时
     * asynchttpclient
     */
    public void postFileAsyncHttpClient()
    {
        try
        {
            String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
            String fileName = "cc.jpg";

            File file = new File(canonicalPath, fileName);
            Log.e(TAG, "postFileAsyncHttpClient: " + file.getCanonicalPath());
            if (file.exists() && file.length() > 0)
            {
                Log.e(TAG, "postFileAsyncHttpClient: " + "file.length() " + file.length());
                final AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("file", file);
                params.put("fileName", fileName);

                client.post(mBasePostFileUrl, params, new AsyncHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                    {
                        Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error)
                    {
                        Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_LONG).show();
                    }
                });
            } else
            {
                Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


}
