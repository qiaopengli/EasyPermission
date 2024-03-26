package com.lqp.base.permission.helper;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.lqp.base.helper.BaseSharedPreferences;

public class PermissionSP extends BaseSharedPreferences {

    private static volatile PermissionSP instance = null;


    public static PermissionSP getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (PermissionSP.class) {
                if (instance == null) {
                    instance = new PermissionSP(context);
                }
            }
        }
        return instance;
    }


    public static final long REQ_PERMISSION_TIME = 48 * 60 * 60 * 1000; //48小时申请一次权限
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    private PermissionSP(@NonNull Context context) {
        super(context);
    }

    public long getPermissionReqTime(String permission) {
        return sp.getLong("PermissionReqTime" + permission, 0);
    }

    public void savePermissionReqTime(String permission, long nowTime) {
        editor.putLong("PermissionReqTime" + permission, nowTime);
    }



    public boolean canRequestPermissions(String... permissions) {
        if (permissions == null) {
            return true;
        }
        StringBuilder builder = new StringBuilder();
        for (String permission : permissions) {
            builder.append(permission);
        }
        long lastTime = getPermissionReqTime(builder.toString());
        long nowTime = System.currentTimeMillis();
        if (nowTime - lastTime >= REQ_PERMISSION_TIME) {
            savePermissionReqTime(builder.toString(), nowTime);
            return true;
        }
        return false;
    }

    public boolean haveRequestedPermissions(String permission) {
        long lastTime = getPermissionReqTime(permission);
        return lastTime > 0;
    }
}
