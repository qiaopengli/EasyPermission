package com.lqp.base.permission;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * 权限申请所需数据
 * credited by qiaopengli
 */
public class EasyPermissionData {
    @NonNull
    public final Context context;
    @NonNull
    public final String[] permissions;
    /**
     * 检查距离上次申请时间是否超过最短限制，多用于非用户手动触发权限申请
     */
    public boolean checkLastTime;

    public EasyPermissionData setCheckLastTime(boolean checkLastTime) {
        this.checkLastTime = checkLastTime;
        return this;
    }

    /**
     * @param context      上下文
     * @param checkLastTime 是否检查距离上次申请时间是否超过最短限制，多用于非用户手动触发权限申请
     * @param permissions    权限
     */
    public EasyPermissionData(@NonNull Context context, boolean checkLastTime, @NonNull String... permissions) {
        this.context = context;
        this.checkLastTime = checkLastTime;
        this.permissions = permissions;
    }
}