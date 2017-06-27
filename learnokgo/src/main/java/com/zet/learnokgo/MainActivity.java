package com.zet.learnokgo;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zet.learnokgo.databinding.ActivityMainBinding;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // keymap[快捷键类型] ==> eclipse
        // ctrl alt f ==> 全局私有变量
        mBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        mBinding.setMain(MainActivity.this);

        requestPermission(); // 请求权限
    }

    private final int request_permission_code = 200; // 请求权限代码
    private final int request_settings_code = 300; // 请求设置代码

    // 权限监听
    private final PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case request_permission_code:
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case request_permission_code:
                    finish();
                    break;
            }
            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                AndPermission.defaultSettingDialog(MainActivity.this, request_settings_code).show();
            }
        }
    };

    // 理由监听
    private final RationaleListener mRationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
            switch (requestCode) {
                case request_permission_code:
                    AndPermission.rationaleDialog(MainActivity.this, rationale).show();
                    break;
            }
        }
    };

    // 请求权限
    private void requestPermission() {
        AndPermission
                .with(this) // this
                .requestCode(request_permission_code) // 请求码
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE) // 权限
                .callback(mPermissionListener) // 回调
                .rationale(mRationaleListener) // 基本理由
                .start(); // 开始请求
    }

    public final ObservableField<String> mTvProgressStr = new ObservableField<>();
    public final ObservableField<String> mTvSpeedStr = new ObservableField<>();
    public final ObservableInt mPbLoadInt = new ObservableInt();

    private static final String fileUrl = "http://s4.cn.bing.net/th?id=OJ.ZlNn60Nq4g90tA&pid=MSNJVFeeds";

    // click 事件方法, 一定要加View参数
    public void mFile1(View view) {
        OkGo.<File>get(fileUrl)
                .tag(fileUrl)
                .execute(new FileCallback() {
                    @Override
                    public void onSuccess(Response<File> response) {
                        File file = response.body();
                        Log.e(TAG, "onSuccess: " + file.getAbsolutePath());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        Log.e(TAG, "downloadProgress: " + progress.fraction);
                        float progressF = progress.fraction * 100;
                        mTvProgressStr.set(String.valueOf(progressF));
                        mTvSpeedStr.set(String.valueOf(progress.speed / 1024));
                        mPbLoadInt.set((int) progressF == 100.0F ? 0 : (int) progressF);
//                        mTvProgress.setText(String.valueOf(progressF));
//                        mTvSpeed.setText(String.valueOf(progress.speed / 1000));
//                        mPbLoad.setProgress((int) progressF);
                    }
                });
    }

    public final ObservableField<Bitmap> mBitmap1Image = new ObservableField<>();

    private static final String imageUrl = "http://s3.cn.bing.net/th?id=OJ.Pwdhu3RnuDYnKg&pid=MSNJVFeeds";

    // 点击事件方法, 一定要填写参数View
    public void mBitmap1(View view) {
        OkGo.<Bitmap>get(imageUrl)
                .tag(imageUrl)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Response<Bitmap> response) {
                        Bitmap bitmap = response.body();
//                        mIvShow.setImageBitmap(bitmap);
//                        mBitmap1Image.set(bitmap);
                        mBinding.mIvShow.setImageBitmap(bitmap); // 公共常量调用
                    }
                });
    }

    public final ObservableField<String> mGet1Html = new ObservableField<>();

    private static final String bingUrl = "http://cn.bing.com/";

    public void mGet1(View view) {
        OkGo.<String>get(bingUrl)
                .tag(bingUrl)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
//                        mTvInfo.setText(response.body());
                        mGet1Html.set(response.body());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(fileUrl);
        OkGo.getInstance().cancelTag(imageUrl);
        OkGo.getInstance().cancelTag(bingUrl);
    }
}
