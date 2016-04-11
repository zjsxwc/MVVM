package com.arialyy.frame.permission;

/**
 * Created by lyy on 2016/4/11.
 * 权限回调
 */
public interface OnPermissionCallback {
    public void onSuccess(String... permissions);
    public void onFail(String... permissions);
}
