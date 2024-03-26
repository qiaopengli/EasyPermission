package com.lqp.base.permission.iface;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.lqp.base.permission.EasyPermissionData;

/**
 * 权限申请所需弹窗获取接口
 * credited by qiaopengli
 */
public interface IPermissionDialogCreator<D extends EasyPermissionData> {
    default IPermissionDialog getRequestDialog(@NonNull Activity activity, @NonNull D data) {
        return null;
    }

    default IPermissionDialog getToSettingDialog(@NonNull Activity activity, @NonNull D data) {
        return null;
    }
}
