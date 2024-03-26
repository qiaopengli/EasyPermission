package com.lqp.base.permission;

/**
 * 权限申请结果
 * <p>
 * granted,//授权成功
 * denied,//拒绝权限
 * cancelSetting,//拒绝去设置 或 不显示去设置
 * forbidByTimes,//不允许请求权限，指定内只允许一次
 * reqRefused,//拒绝请求权限--指app弹窗拒绝
 * error //其他异常情况
 */
public enum PermissionResult {
    unStatement,//未声明权限，Manifest中未声明该权限
    granted,//授权成功
    denied,//拒绝权限
    cancelSetting,//拒绝去设置 或 不显示去设置
    forbidByTimes,//不允许请求权限，指定内只允许一次
    reqRefused,//拒绝请求权限--指app弹窗拒绝
    error //其他异常情况
}
