package com.lqp.base.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lqp.base.BaseCallbackActivity;
import com.lqp.base.permission.iface.IPermissionRequestCallback;

public class PermissionRequestActivity extends BaseCallbackActivity {
    private static final String PARAMS_CALL_BACK_KEY = "callBackKey";
    private static final String PARAMS_PERMISSIONS = "permissions";
    private static final int REQ_CODE_PERMISSION_SETTING = 1;
    private static final int REQ_CODE_PERMISSION_REQUEST = 2;

    public static void startRequestPermission(@NonNull Context c, IPermissionRequestCallback cb, String... permissions) {
        SortPermissions sortPermissions = new SortPermissions(c, permissions);

        Intent i = new Intent(c, PermissionRequestActivity.class);
        String key = putObjToTmp(cb, iPermissionRequestCallback
                -> iPermissionRequestCallback.onDenied(PermissionResult.error, sortPermissions.sort(c))
        );
        i.putExtra(PARAMS_CALL_BACK_KEY, key);
        i.putExtra(PARAMS_PERMISSIONS, permissions);
        if (!(c instanceof Activity)) {
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        c.startActivity(i);
    }

    private IPermissionRequestCallback mCallBack;
    private SortPermissions sortPermissions;
    private long startRequestTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null) {
            finish();
            return;
        }

        String key = getIntent().getStringExtra(PARAMS_CALL_BACK_KEY);
        mCallBack = getObjFromTmpAndRemove(key);
        if (mCallBack == null) {
            finish();
            return;
        }
        sortPermissions = new SortPermissions(this, getIntent().getStringArrayExtra(PARAMS_PERMISSIONS));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstResume()) {
            mCallBack.beforeRequest(this, new IPermissionRequestCallback.IHandler() {
                @Override
                public void executor() {
                    requestPermissions();
                }

                @Override
                public void cancel() {
                    notifyDenied(PermissionResult.reqRefused);
                }
            });
        }
    }

    public void requestPermissions() {
        startRequestTime = System.currentTimeMillis();
        requestPermissions(sortPermissions.getAllPermissions().toArray(new String[0])
                , REQ_CODE_PERMISSION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PERMISSION_SETTING) {
            sortPermissions.sort(this);
            if (sortPermissions.isGranted()) {
                notifyGranted();
            } else {
                notifyDenied(PermissionResult.denied);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQ_CODE_PERMISSION_REQUEST) {
            return;
        }
        //如果请求权限到onRequestPermissionsResult时间差大于350ms认为有系统权限申请弹窗弹出，不再弹窗去设置。否则尝试弹窗去设置
        boolean hasSystemDialog = System.currentTimeMillis() - startRequestTime > 350;
        sortPermissions.sort(this);

        if (sortPermissions.getDeniedList().isEmpty()) {
            notifyGranted();
        } else {
            if (EasyPermissionUtil.hasAlwaysDeniedPermission(this, sortPermissions.getDeniedList())
                    && !hasSystemDialog) {
                mCallBack.onCannotSystemProgress(this, new IPermissionRequestCallback.IHandler() {
                    @Override
                    public void executor() {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivityForResult(intent, REQ_CODE_PERMISSION_SETTING);
                    }

                    @Override
                    public void cancel() {
                        notifyDenied(PermissionResult.cancelSetting);
                    }
                });
            } else {
                notifyDenied(PermissionResult.denied);
            }
        }
    }

    private void notifyGranted() {
        mCallBack.onGranted(sortPermissions);
        finish();
    }

    private void notifyDenied(PermissionResult result) {
        mCallBack.onDenied(result, sortPermissions);
        finish();
    }

}
