package com.lqp.permission

import com.lqp.base.permission.EasyPermissionUtil
import com.lqp.base.permission.PermissionResult

class EasyPermissionCallbackKT(
    val denied: (result: PermissionResult) -> Unit,
    val granted: () -> Unit
) : EasyPermissionUtil.EasyPermissionCallback {
    override fun onDenied(result: PermissionResult) {
        denied(result)
    }

    override fun onGranted() {
        granted()
    }

}