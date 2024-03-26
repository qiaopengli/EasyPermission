package com.lqp.base.permission.iface;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.lqp.base.permission.PermissionResult;
import com.lqp.base.permission.SortPermissions;

/**
 * 权限申请回调
 * credited by qiaopengli
 */
public interface IPermissionRequestCallback {

    interface IHandler {
        /**
         * 执行预期动作
         */
        void executor();

        /**
         * 取消预期动作
         */
        void cancel();
    }

    /**
     * 权限全部授权
     *
     * @param sortPermissions 可以通过以下方法查看所有授权权限：
     *                        see{@link SortPermissions#getGrantedList()}
     *                        or {@link SortPermissions#getAllPermissions()}
     */
    void onGranted(SortPermissions sortPermissions);

    /**
     * 未全部授权
     *
     * @param result          拒绝的原因
     * @param sortPermissions 可以通过以下方法查看已授权/未授权权限
     *                        see {@link SortPermissions#getGrantedList()}
     *                        /{@link SortPermissions#getDeniedList()}
     */
    void onDenied(PermissionResult result, SortPermissions sortPermissions);

    /**
     * 申请权限前
     *
     * @param activity 上下文
     * @param handler  {@link IHandler#executor()}开始申请  {@link IHandler#cancel()}中断申请
     */
    default void beforeRequest(@NonNull Activity activity, @NonNull IHandler handler) {
        handler.executor();
    }

    /**
     * 无法执行系统授权流程，用户可以在此提醒用户去设置
     *
     * @param activity 上下文
     * @param handler  {@link IHandler#executor()}去设置  {@link IHandler#cancel()}不去设置
     */
    default void onCannotSystemProgress(@NonNull Activity activity, @NonNull IHandler handler) {
        handler.cancel();
    }
}
