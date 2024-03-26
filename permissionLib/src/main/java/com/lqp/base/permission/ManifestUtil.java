package com.lqp.base.permission;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ManifestUtil {
    /**
     * Return the permissions used in application.
     *
     * @param context context.
     * @return the permissions used in application
     */
    public static List<String> getPermissionsInApp(@NonNull Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return Arrays.asList(
                    pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS)
                            .requestedPermissions
            );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 是否声明了这些权限
     *
     * @param context     上下文
     * @param permissions 权限数组
     * @return true:全部声明  false:部分或全部未声明
     */
    public static boolean isPermissionStatementInApp(@NonNull Context context, String... permissions) {
        if (permissions == null) {
            return true;
        }
        List<String> statements = getPermissionsInApp(context);
        if (statements == null) {
            return false;
        }
        for (String permission : permissions) {
            if (!statements.contains(permission)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 获取application目录下的mata-data属性
     *
     * @param c   上下文
     * @param key
     * @return nullable
     */
    @Nullable
    public static String readAppMataData(Context c, String key) {
        try {
            ApplicationInfo ai = c.getPackageManager().getApplicationInfo(
                    c.getPackageName(), PackageManager.GET_META_DATA);
            Object data = ai.metaData.get(key);
            if (data instanceof Integer) {
                long longValue = ((Integer) data).longValue();
                return String.valueOf(longValue);
            } else if (data instanceof String) {
                return String.valueOf(data);
            } else {
                return null;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
