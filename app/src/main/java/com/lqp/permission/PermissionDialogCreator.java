package com.lqp.permission;

import android.app.Activity;
import android.app.AlertDialog;

import androidx.annotation.NonNull;

import com.lqp.base.permission.iface.IPermissionDialog;
import com.lqp.base.permission.iface.IPermissionDialogCreator;
import com.lqp.base.permission.iface.OnOperationListener;

public class PermissionDialogCreator implements IPermissionDialogCreator<MyEasyPermissionData> {
    @Override
    public IPermissionDialog getRequestDialog(@NonNull Activity activity, @NonNull MyEasyPermissionData data) {
        return new IPermissionDialog() {
            OnOperationListener onOperationListener = null;

            @Override
            public void show() {
                new AlertDialog.Builder(activity).setCancelable(false)
                        .setTitle(data.title)
                        .setMessage(data.msg)
                        .setIcon(data.iconRes)
                        .setPositiveButton("允许", (dialog, which) -> {
                            if (onOperationListener != null) {
                                onOperationListener.onConfirm();
                            }
                        })
                        .setNegativeButton("拒绝", (dialog, which) -> {
                            if (onOperationListener != null) {
                                onOperationListener.onCancel();
                            }
                        })
                        .show();


            }

            @Override
            public IPermissionDialog setOperationCallback(OnOperationListener listener) {
                onOperationListener = listener;
                return this;
            }
        };
    }

    @Override
    public IPermissionDialog getToSettingDialog(@NonNull Activity activity, @NonNull MyEasyPermissionData data) {
        return new IPermissionDialog() {
            OnOperationListener onOperationListener = null;

            @Override
            public void show() {
                new AlertDialog.Builder(activity).setCancelable(false)
                        .setTitle(data.settingTitle)
                        .setMessage(data.settingMsg)
                        .setIcon(data.iconRes)
                        .setPositiveButton("去设置", (dialog, which) -> {
                            if (onOperationListener != null) {
                                onOperationListener.onConfirm();
                            }
                        })
                        .setNegativeButton("拒绝", (dialog, which) -> {
                            if (onOperationListener != null) {
                                onOperationListener.onCancel();
                            }
                        })
                        .show();


            }

            @Override
            public IPermissionDialog setOperationCallback(OnOperationListener listener) {
                onOperationListener = listener;
                return this;
            }
        };
    }
}
