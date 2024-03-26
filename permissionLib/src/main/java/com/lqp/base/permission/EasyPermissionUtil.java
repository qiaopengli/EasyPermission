package com.lqp.base.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.AppOpsManagerCompat;
import androidx.core.content.ContextCompat;

import com.lqp.base.permission.helper.PermissionSP;
import com.lqp.base.permission.iface.IPermissionDialog;
import com.lqp.base.permission.iface.IPermissionDialogCreator;
import com.lqp.base.permission.iface.IPermissionRequestCallback;
import com.lqp.base.permission.iface.OnOperationListener;

import java.util.List;

public class EasyPermissionUtil {
    public interface EasyPermissionCallback {
        default void onGranted() {
        }

        default void onDenied(@NonNull PermissionResult result) {
        }
    }

    public static <D extends EasyPermissionData> void reqPermission(D data) {
        reqPermission(data, null);
    }

    public static <D extends EasyPermissionData> void reqPermission(D data
            , @Nullable EasyPermissionCallback iCallback) {
        reqPermission(data, null, iCallback);
    }

    public static <D extends EasyPermissionData> void reqPermission(D data
            , @Nullable IPermissionDialogCreator<D> inputCreator
            , @Nullable EasyPermissionCallback iCallback) {
        EasyPermissionCallback callback = iCallback == null ? new EasyPermissionCallback() {
        } : iCallback;
        if (data == null) {
            callback.onDenied(PermissionResult.unStatement);
            return;
        }

        if (!ManifestUtil.isPermissionStatementInApp(data.context, data.permissions)) {//部分或全部权限未声明
            callback.onDenied(PermissionResult.unStatement);
        } else if (EasyPermissionUtil.hasPermissions(data.context, data.permissions)) {//已有权限直接获取
            callback.onGranted();
        } else if (!data.checkLastTime
                || PermissionSP.getInstance(data.context).canRequestPermissions(data.permissions)) {//无权限，允许请求权限
            EasyPermissionUtil.requestPermissions(data.context, new IPermissionRequestCallback() {
                @Override
                public void onGranted(SortPermissions sortPermissions) { //授权
                    callback.onGranted();
                }

                @Override
                public void onDenied(PermissionResult result, SortPermissions sortPermissions) {
                    callback.onDenied(result);
                }

                @Override
                public void beforeRequest(@NonNull Activity activity, @NonNull IPermissionRequestCallback.IHandler handler) {
                    IPermissionDialog requestDialog = null;
                    if (inputCreator != null) {
                        requestDialog = inputCreator.getRequestDialog(activity, data);
                    }
                    if (requestDialog == null) {
                        handler.executor();
                    } else {
                        requestDialog.setOperationCallback(new OnOperationListener() {
                            @Override
                            public void onCancel() {//不允许
                                handler.cancel();
                            }

                            @Override
                            public void onConfirm() {//允许
                                handler.executor();
                            }
                        });
                        requestDialog.show();
                    }
                }

                @Override
                public void onCannotSystemProgress(@NonNull Activity activity, @NonNull IHandler handler) { //一直被拒绝 弹窗提醒去权限设置页
                    IPermissionDialog toSettingDialog = null;
                    if (inputCreator != null) {
                        toSettingDialog = inputCreator.getToSettingDialog(activity, data);
                    }
                    if (toSettingDialog == null) {//没有去设置弹窗则直接中断操作
                        handler.cancel();
                    } else {
                        toSettingDialog.setOperationCallback(new OnOperationListener() {
                            @Override
                            public void onCancel() {
                                handler.cancel();
                            }

                            @Override
                            public void onConfirm() {
                                handler.executor();
                            }
                        });
                        toSettingDialog.show();
                    }
                }
            }, data.permissions);
        } else {//无权限，不允许请求权限
            callback.onDenied(PermissionResult.forbidByTimes);
        }
    }

    public static void requestPermissions(Context c, IPermissionRequestCallback callback, String... ps) {
        SortPermissions sortPermissions = new SortPermissions(c, ps);
        if (sortPermissions.isGranted()) {
            callback.onGranted(sortPermissions);
        } else {
            PermissionRequestActivity.startRequestPermission(c, callback, ps);
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        //红米 note9 系统 MUI版本12.0.9
        //java.lang.SecurityException: Proxy package com.creditease.dian from uid 10297
        // or calling package com.xxx from uid 10297 not allowed to perform REQUEST_INSTALL_PACKAGES
        try {
            for (String permission : permissions) {
                String op = AppOpsManagerCompat.permissionToOp(permission);
                if (TextUtils.isEmpty(op)) {
                    continue;
                }
                int result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
                if (result == AppOpsManagerCompat.MODE_IGNORED) return false;
                result = ContextCompat.checkSelfPermission(context, permission);
                if (result != PackageManager.PERMISSION_GRANTED) return false;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 在华为手机尝试该方法没有意义，总是返回true
     */
    @SuppressLint("ObsoleteSdkInt")
    public static boolean hasAlwaysDeniedPermission(Activity activity, List<String> deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
        for (String permission : deniedPermissions) {
            if (!activity.shouldShowRequestPermissionRationale(permission)) {
                return true;
            }
        }
        return false;
    }
}
