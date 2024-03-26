package com.lqp.base.permission.iface;

public interface IPermissionDialog {
    void show();

    IPermissionDialog setOperationCallback(OnOperationListener listener);
}
