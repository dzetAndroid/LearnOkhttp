package com.zet.learnokhttp3;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zet.learnokhttp3.app.IApp;
import com.zet.learnokhttp3.bean.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

import static com.zhy.http.okhttp.OkHttpUtils.post;

/**
 * 主活动Activity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 静态常量TAG
     */
    private static final String TAG = "MainActivity";
    private static final int request_code_permission = 100;
    private static final int request_code_settings = 300;
    private static String mBaseUrl = "http://192.168.137.1:8080/web/";

    private TextView mTVInfo;
    private Button mBtnGet1;
    private Button mBtnGet2;
    private Button mBtnPost1;
    private Button mBtnPostJSON1;
    private Button mBtnPostFile1;
    private Button mBtnPostFormFile1;
    private Button mBtnDown1;
    private ImageView mIVShow;
    private Button mBtnDownAndShowImage1;
    private Button mBtnDownAndShowProgress1;
    private ProgressBar mPBShow;
    private TextView mTVNum;

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
            requestPermission();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        AndPermission
                .with(this)
                .requestCode(request_code_permission)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        switch (requestCode) {
                            case request_code_permission:
                                initView();
                                break;
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        switch (requestCode) {
                            case request_code_permission:
//                                finish();
                                Toast.makeText(MainActivity.this, "当前页面暂时无法使用", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                            AndPermission.defaultSettingDialog(MainActivity.this, request_code_settings).show();
                        }
                    }
                })
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(MainActivity.this, rationale).show();
                    }
                })
                .start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtnGet1:
                getJ2EEWeb();
                break;
            case R.id.mBtnGet2:
                getInternetHtml();
                break;
            case R.id.mBtnPost1:
                btnPost();
                break;
            case R.id.mBtnPostJSON1:
                btnPostJSON1();
                break;
            case R.id.mBtnPostFile1:
                btnPostFile1();
                break;
            case R.id.mBtnPostFormFile1:
                btnFormFile1();
                break;
            case R.id.mBtnDown1:
                btnDown1();
                break;
            case R.id.mBtnDownAndShowImage1:
                btnDownAndShowImage1();
                break;
            case R.id.mBtnDownAndShowProgress1:
                mBtnDownAndShowProgress1();
                break;
        }
    }

    /**
     * get file to pregress show
     */
    private void mBtnDownAndShowProgress1() {
        try {
            String url = "http://filelx.liqucn.com/upload/2017/302/p/Amap_V8.0.8.2180_android_C02110001851_Build1705252208.apk";
            String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
            String fileName = "Amap_V8.0.8.2180_android_C02110001851_Build1705252208.apk";

            OkHttpUtils
                    .get()
                    .url(url)
                    .build()
                    .execute(new FileCallBack(canonicalPath, fileName) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(File response, int id) {
                            Log.e(TAG, "onResponse: " + response.getAbsolutePath());
                        }

                        @Override
                        public void inProgress(float progress, long total, int id) {
                            Log.e(TAG, "inProgress: " + progress + " " + total);
                            float i = progress * 100;
                            // android.content.res.Resources$NotFoundException: String resource ID #0x0
                            mTVNum.setText(String.valueOf(i));
                            mPBShow.setProgress((int) i);
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get file to image show it
     * bitmapcallback
     */
    private void btnDownAndShowImage1() {
        try {
            String imageUrl = "http://hukai.me/android-training-course-in-chinese/basics/actionbar/actionbar-actions.png";

            OkHttpUtils
                    .get()
                    .url(imageUrl)
                    .build()
                    .execute(new BitmapCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Bitmap response, int id) {
                            mIVShow.setImageBitmap(response);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * get file
     * filecallback(parentPath, fileName)
     */
    private void btnDown1() {
        try {
            String imageUrl = "http://img.blog.csdn.net/20170620081902491?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFkdXhpbmd6aGU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast";
            String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
            String fileName = "20170620081902491.png";

            OkHttpUtils
                    .get() // 指定类型
                    .url(imageUrl) // 指定地址
                    .build() // 构造
                    .execute(new FileCallBack(canonicalPath, fileName) { // 执行
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(File response, int id) {
                            Log.e(TAG, "onResponse: " + response.getAbsolutePath());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * post form file
     */
    private void btnFormFile1() {
        try {
            String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
            String fileName = "android-debug.apk";
            File file = new File(canonicalPath, fileName);


            OkHttpUtils
                    .post()
                    .addFile("mPhoto", fileName, file)
                    .url(mBaseUrl + "User_uploadInfo")
                    .addParams("username", "username_from_1")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            mTVInfo.setText(response);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * post file
     */
    private void btnPostFile1() {

        try {
            String canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
            String fielName = "22880.txt";
            File file = new File(canonicalPath, fielName);

            OkHttpUtils
                    .postFile()
                    .url(mBaseUrl + "User_postStream")
                    .file(file)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            mTVInfo.setText(response);
                        }
                    });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * post string-json
     */
    private void btnPostJSON1() {
        OkHttpUtils
                .postString()
                .url(mBaseUrl + "User_postString")
                .content(IApp.getGson().toJson(new User("u_4568412", "p_541284")))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mTVInfo.setText(response);
                    }
                });
    }

    /**
     * post
     */
    private void btnPost() {
        post()
                .url(mBaseUrl + "User_login")
                .addParams("username", "post_u_1")
                .addParams("password", "post_p_1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mTVInfo.setText(response);
                    }
                });

    }

    /**
     * get 请求 j2ee服务器
     */
    private void getJ2EEWeb() {
        String url = mBaseUrl + "User_login";
        OkHttpUtils
                .get() // 请求类型
                .url(url) // 指定地址
                .addParams("username", "user_1") // 参数
                .addParams("password", "pass_1")
                .build() // 构造
                .execute(new StringCallback() { // 执行
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) { // UI线程
                        mTVInfo.setText(response);
                    }
                });
    }

    /**
     * get 请求 随意网址
     */
    private void getInternetHtml() {
        String url = "http://www.piaotian.com/";
        OkHttpUtils
                .get() // 请求类型
                .url(url) // 指定地址
                .build() // 构造
                .execute(new StringCallback() { // 执行
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) { // UIThread
                        Log.e(TAG, "onResponse: " + response);
                        mTVInfo.setText(response);
                    }
                });
    }

    private void initView() {
        mTVInfo = (TextView) findViewById(R.id.mTVInfo);

        mBtnGet1 = (Button) findViewById(R.id.mBtnGet1);
        mBtnGet2 = (Button) findViewById(R.id.mBtnGet2);
        mBtnPost1 = (Button) findViewById(R.id.mBtnPost1);

        mBtnGet1.setOnClickListener(this);
        mBtnGet2.setOnClickListener(this);
        mBtnPost1.setOnClickListener(this);
        mBtnPostJSON1 = (Button) findViewById(R.id.mBtnPostJSON1);
        mBtnPostJSON1.setOnClickListener(this);
        mBtnPostFile1 = (Button) findViewById(R.id.mBtnPostFile1);
        mBtnPostFile1.setOnClickListener(this);
        mBtnPostFormFile1 = (Button) findViewById(R.id.mBtnPostFormFile1);
        mBtnPostFormFile1.setOnClickListener(this);
        mBtnDown1 = (Button) findViewById(R.id.mBtnDown1);
        mBtnDown1.setOnClickListener(this);
        mIVShow = (ImageView) findViewById(R.id.mIVShow);
        mIVShow.setOnClickListener(this);
        mBtnDownAndShowImage1 = (Button) findViewById(R.id.mBtnDownAndShowImage1);
        mBtnDownAndShowImage1.setOnClickListener(this);
        mBtnDownAndShowProgress1 = (Button) findViewById(R.id.mBtnDownAndShowProgress1);
        mBtnDownAndShowProgress1.setOnClickListener(this);
        mPBShow = (ProgressBar) findViewById(R.id.mPBShow);
        mPBShow.setOnClickListener(this);
        mTVNum = (TextView) findViewById(R.id.mTVNum);
        mTVNum.setOnClickListener(this);
    }
}
