package com.zet.learnokhttp3;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 主活动
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 静态常量TAG
     */
    private static final String TAG = "MainActivity";

    /**
     * 静态常量okhttpclient
     */
    private static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

    private static final String mBaseUrl = "http://192.168.137.1:8080/web/";

    /**
     * btn get1
     */
    private Button mBtnGet1;
    /**
     * textview info
     */
    private TextView mTVInfo;
    private Button mBtnPost1;
    private Button mBtnFile1;
    private Button mBtnDown1;
    private ImageView mIVInfo;
    private Button mBtnDown2;
    private Button mBtnMulti1;

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
            initOkhttp3();
            requestPermission();
        }
    }

    private void initOkhttp3() {
        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        OK_HTTP_CLIENT = builder.build();
    }

    /**
     * 请求码 权限存储
     */
    private static final int request_code_permission = 100;
    /**
     * 请求码 设置
     */
    private static final int request_code_settings = 337;

//    private final PermissionListener permissionListener = new PermissionListener() { // 回调
//        @Override
//        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
//            switch (requestCode){
//                case request_code_permission:
//                    initView(); // success, initView()
//                    break;
//            }
//        }
//
//        @Override
//        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
//            switch (requestCode){
//                case request_code_permission:
//                    finish(); // fail, finish()
//                    break;
//            }
//            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)){
//                AndPermission.defaultSettingDialog(MainActivity.this, request_code_settings).show();
//            }
//        }
//    };
//
//    private final RationaleListener rationaleListener = new RationaleListener() { // 基本理由
//        @Override
//        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
//            AndPermission.rationaleDialog(MainActivity.this, rationale).show();
//        }
//    };

    /**
     * 请求权限
     */
    private void requestPermission() {
        AndPermission
                .with(this) // this
                .requestCode(request_code_permission) // 请求码
                .permission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) // 权限
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        switch (request_code_permission) {
                            case request_code_permission:
                                initView();
                                break;
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        switch (request_code_permission) {
                            case request_code_permission:
                                finish();
                                break;
                        }
                        if (AndPermission.hasAlwaysDeniedPermission(
                                MainActivity.this, deniedPermissions)) {
                            AndPermission.defaultSettingDialog(
                                    MainActivity.this, request_code_settings).show();
                        }
                    }
                }) // 回调
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(
                            int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(MainActivity.this, rationale).show();
                    }
                }) // 基本理由
                .start(); // 开始请求
    }

    /**
     * 找到视图ID
     */
    private void initView() {
        mTVInfo = (TextView) findViewById(R.id.mTVInfo);

        mBtnGet1 = (Button) findViewById(R.id.mBtnGet1);
        mBtnGet1.setOnClickListener(this);
        mBtnPost1 = (Button) findViewById(R.id.mBtnPost1);
        mBtnPost1.setOnClickListener(this);
        mBtnFile1 = (Button) findViewById(R.id.mBtnFile1);
        mBtnFile1.setOnClickListener(this);
        mBtnDown1 = (Button) findViewById(R.id.mBtnDown1);
        mBtnDown1.setOnClickListener(this);
        mIVInfo = (ImageView) findViewById(R.id.mIVInfo);
        mIVInfo.setOnClickListener(this);
        mBtnDown2 = (Button) findViewById(R.id.mBtnDown2);
        mBtnDown2.setOnClickListener(this);
        mBtnMulti1 = (Button) findViewById(R.id.mBtnMulti1);
        mBtnMulti1.setOnClickListener(this);
    }

    /**
     * 视图点击处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtnGet1:
                btnGet1();
                break;
            case R.id.mBtnPost1:
                btnPost1();
                break;
            case R.id.mBtnFile1:
                btnFile1();
                break;
            case R.id.mBtnDown1:
                btnDown1();
                break;
            case R.id.mBtnDown2:
                btnDown2();
                break;
            case R.id.mBtnMulti1:
                btnMutlti1();
                break;
        }
    }

    /**
     * 异步 multi
     */
    private void btnMutlti1() {
        try {
            String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
            String fielName = "sdl_log.txt";
            File file = new File(canonicalPath, fielName);
            // mediatype 文件类型 application/octet-stream
            RequestBody fileBody = RequestBody.create(
                    MediaType.parse("application/octet-stream"), file);

            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
            MultipartBody multipartBody = multipartBodyBuilder
                    .setType(MultipartBody.FORM) // multitype 指定类型 Multipart.FORM
                    .addFormDataPart("username", "user_multi_1")
                    .addFormDataPart("password", "pass_multi_1")
                    .addFormDataPart("mPhoto", fielName, fileBody)
                    .build();

            Request.Builder requestBuilder = new Request.Builder();
            Request request = requestBuilder
                    .post(multipartBody)
                    .url(mBaseUrl + "User_uploadInfo")
                    .build();

            Call call = OK_HTTP_CLIENT.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String string = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTVInfo.setText(string);
                        }
                    });
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步 down2
     */
    private void btnDown2() {
        String imgUrl = "http://www.baoliny.com/files/article/image/1/1991/1991s.jpg";
        int beginIndex = imgUrl.lastIndexOf("/") + 1;
        final String fileName = imgUrl.substring(beginIndex);
        Log.e(TAG, "btnDown2: fileName " + fileName);

        Request.Builder builder = new Request.Builder();
        Request request = builder
                .get()
                .url(imgUrl)
                .build();

        Call call = OK_HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();

                InputStream inputStream = response.body().byteStream();
                File file = new File(canonicalPath, fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                int len = 0;
                byte[] buf = new byte[128];
                while ((len = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, len);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();

            }
        });

    }

    /**
     * 异步 down1
     */
    private void btnDown1() {
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .get()
                .url(mBaseUrl + "files/20160421142555512.png")
                .build();

        Call call = OK_HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIVInfo.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    /**
     * 异步 file
     */
    private void btnFile1() {
        try {
            String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
            String fileName = "22880.txt";
            File file = new File(canonicalPath, fileName);
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet"), file);

            Request.Builder builder = new Request.Builder();
            Request request = builder
                    .post(fileBody)
                    .url(mBaseUrl + "User_postStream")
                    .build();

            Call call = OK_HTTP_CLIENT.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String string = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTVInfo.setText(string);
                        }
                    });
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步 post
     */
    private void btnPost1() {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        FormBody formBody = formBodyBuilder
                .add("username", "user_post_1")
                .add("password", "pass_post_1")
                .build();

        Request.Builder builder = new Request.Builder();
        Request request = builder
                .post(formBody)
                .url(mBaseUrl + "User_login")
                .build();

        Call call = OK_HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTVInfo.setText(string);
                    }
                });
            }
        });
    }

    /**
     * 异步 post
     * username
     * password
     */
    private void btnGet1() {
        String url = "http://www.piaotian.com/";

        Request.Builder builder = new Request.Builder(); // 请求构造者
        Request request = builder
                .get()
                .url(url)
                .build(); // 构造请求

        Call call = OK_HTTP_CLIENT.newCall(request); // 新建一个呼叫
        call.enqueue(new Callback() { // 队列呼叫
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException { // 成功
                final String string = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTVInfo.setText(string); // textview显示
                    }
                });
            }
        });
    }
}
