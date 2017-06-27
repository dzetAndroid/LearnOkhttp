package com.zet.learnokgo.tip;

/**
 * Created by Zet on 2017/6/26.
 */

public class AndPermissionTip {

//    private final int request_permission_code = 200;
//    private final int request_settings_code = 300;
//
//    private final PermissionListener mPermissionListener = new PermissionListener() {
//        @Override
//        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
//            switch (requestCode) {
//                case request_permission_code:
//                    initView();
//                    break;
//            }
//        }
//
//        @Override
//        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
//            switch (requestCode) {
//                case request_permission_code:
//                    finish();
//                    break;
//            }
//            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
//                AndPermission.defaultSettingDialog(MainActivity.this, request_settings_code).show();
//            }
//        }
//    };
//
//    private final RationaleListener mRationaleListener = new RationaleListener() {
//        @Override
//        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
//            switch (requestCode) {
//                case request_permission_code:
//                    AndPermission.rationaleDialog(MainActivity.this, rationale).show();
//                    break;
//            }
//        }
//    };
//
//    private void requestPermission() {
//        AndPermission
//                .with(this) // this
//                .requestCode(request_permission_code) // 请求码
//                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE) // 权限
//                .callback(mPermissionListener) // 回调
//                .rationale(mRationaleListener) // 基本理由
//                .start(); // 开始请求
//    }

}
