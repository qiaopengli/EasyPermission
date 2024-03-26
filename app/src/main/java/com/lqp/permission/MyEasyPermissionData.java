package com.lqp.permission;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.lqp.base.permission.EasyPermissionData;

public class MyEasyPermissionData extends EasyPermissionData {
    public String title;
    public String msg;
    @DrawableRes
    public int iconRes;
    public String settingTitle;
    public String settingMsg;

    /**
     * @param context       上下文
     * @param checkLastTime 是否检查距离上次申请时间是否超过最短限制，多用于非用户手动触发权限申请
     * @param permissions   权限
     */
    public MyEasyPermissionData(@NonNull Context context, boolean checkLastTime, @NonNull String... permissions) {
        super(context, checkLastTime, permissions);
    }

    public MyEasyPermissionData setTitle(String title) {
        this.title = title;
        return this;
    }

    public MyEasyPermissionData setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public MyEasyPermissionData setIconRes(int iconRes) {
        this.iconRes = iconRes;
        return this;
    }

    public MyEasyPermissionData setSettingMsg(String settingMsg) {
        this.settingMsg = settingMsg;
        return this;
    }

    public MyEasyPermissionData setSettingTitle(String settingTitle) {
        this.settingTitle = settingTitle;
        return this;
    }
}
