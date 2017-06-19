package com.zet.learnokhttp2;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.zet.learnokhttp2.app.IApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 主活动页面
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final String mBaseUrl = "http://192.168.137.1:8080/web/";

    private TextView mTvResult;
    private Button mBtnPostStream;
    private Button mBtnPostMulti;
    private Button mBtnDown;
    private ImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        getPermission();
    }

    /**
     * 获取文件读写权限
     */
    private void getPermission() {
        AndPermission
                .with(getApplicationContext()) // 1 activity fragment context
                .requestCode(200) // 2 请求码
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE) // 3 请求的权限
                .callback(new PermissionListener() {
                    // 成功
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions) {
                        if (requestCode == 200) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTvResult.setText("permission is ok");
                                }
                            });
                        }
                    }

                    // 失败
                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        if (requestCode == 200) {
                            finish(); // 销毁当前Activity
                            // 权限没拿到, 功能没得用哎
                        }
                    }
                }) // 4 回调
                .start(); // 5 开始（请求）
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtnPostStream:
                postStream();
                break;
            case R.id.mBtnPostMulti:
                postMulti();
                break;
            case R.id.mBtnDown:
                downFile();
                break;
        }
    }

    private void downFile() {
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(mBaseUrl + "files/20160421142555512.png")
                .get()
                .build();

        Call call = IApplication.okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {

                String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();

                InputStream inputStream = response.body().byteStream();

//                BitmapFactory.Options
                // 1 本地 options
                // 2 流 options
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImg.setImageBitmap(bitmap);
                    }
                });

//                File file = new File(canonicalPath, "20160421142555512.png");
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
//
//                int len = 0;
//                byte[] buf = new byte[128];
//
//                while ((len = inputStream.read(buf)) != -1) {
//                    fileOutputStream.write(buf, 0, len);
//                }
//
//                fileOutputStream.flush();
//                fileOutputStream.close();
//                inputStream.close();
//
//                Log.e(TAG, "onResponse: " + " down success");
            }
        });

    }

    private void postMulti() {

        try {
            String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
            String fileName = "cc.jpg";
            File file = new File(canonicalPath, fileName);

            if (!file.exists()) {
                return;
            }

            Log.e(TAG, "postMulti: " + file.getAbsolutePath());
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

            MultipartBuilder multipartBuilder = new MultipartBuilder();
            RequestBody requestBody = multipartBuilder
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("username", "hmj")
                    .addFormDataPart("password", "123")
                    .addFormDataPart("mPhoto", fileName, fileBody)
                    .build();

            Request.Builder builder = new Request.Builder();
            Request request = builder
                    .url(mBaseUrl + "uploadInfo")
                    .post(requestBody)
                    .build();

            Call call = IApplication.okHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String string = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvResult.setText(string);
                        }
                    });
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void postStream() {
        try {
            String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
            String fileName = "cc.jpg";

            File file = new File(canonicalPath, fileName);
            Log.e(TAG, "file.length(): " + file.length());
            Log.e(TAG, "file.getAbsolutePath(): " + file.getAbsolutePath());
            if (!file.exists()) {
                return;
            }

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

            Request request = new Request.Builder()
                    .url(mBaseUrl + "postStream")
                    .post(requestBody)
                    .build();

            Call call = IApplication.okHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String string = response.body().string();
                    Log.e(TAG, "onResponse: " + string);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mTvResult = (TextView) findViewById(R.id.mTvResult);
        mBtnPostStream = (Button) findViewById(R.id.mBtnPostStream);
        mBtnPostMulti = (Button) findViewById(R.id.mBtnPostMulti);

        mBtnPostStream.setOnClickListener(this);
        mBtnPostMulti.setOnClickListener(this);
        mBtnDown = (Button) findViewById(R.id.mBtnDown);
        mBtnDown.setOnClickListener(this);
        mImg = (ImageView) findViewById(R.id.mImg);
        mImg.setOnClickListener(this);
    }


//    /**
//     * 上传文件
//     * 小文件还行（1MB以下）
//     * 大文件, 绝对要死, 总是超时
//     * asynchttpclient
//     */
//    public void postFileAsyncHttpClient() {
//        try {
//            String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
//            String fileName = "cc.jpg";
//
//            File file = new File(canonicalPath, fileName);
//            Log.e(TAG, "postFileAsyncHttpClient: " + file.getCanonicalPath());
//            if (file.exists() && file.length() > 0) {
//                Log.e(TAG, "postFileAsyncHttpClient: " + "file.length() " + file.length());
//                final AsyncHttpClient client = new AsyncHttpClient();
//                RequestParams params = new RequestParams();
//                params.put("file", file);
//                params.put("fileName", fileName);
//
//                client.post(mBaseUrlPostStream, params, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers,
//                                          byte[] responseBody, Throwable error) {
//                        Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_LONG).show();
//                    }
//                });
//            } else {
//                Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

}
