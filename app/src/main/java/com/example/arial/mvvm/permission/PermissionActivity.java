package com.example.arial.mvvm.permission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.permission.OnPermissionCallback;
import com.arialyy.frame.permission.PermissionManager;
import com.arialyy.frame.temp.AbsTempView;
import com.arialyy.frame.util.show.T;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.base.BaseActivity;
import com.example.arial.mvvm.databinding.ActivityPermissionBinding;

import java.util.Arrays;

/**
 * Created by lyy on 2016/4/12.
 * 权限测试Activity
 */
public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> implements OnPermissionCallback {

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle("Activity 权限测试");
    }

    protected int setLayoutId() {
        return R.layout.activity_permission;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.permission:
                PermissionManager.getInstance().requestPermission(this, this, Manifest.permission.SEND_SMS);
                break;
            case R.id.permissions:
                PermissionManager.getInstance().requestPermission(this, this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE});
                break;
            case R.id.show_fragment:
                startActivity(new Intent(this, FragmentContentActivity.class));
                break;
            case R.id.alert_window:
                PermissionManager.getInstance().requestAlertWindowPermission(this, this);
                break;
            case R.id.write_setting:
                PermissionManager.getInstance().requestWriteSettingPermission(this, this);
                break;
        }
    }

    @Override
    public void onSuccess(String... permissions) {
        T.showShort(PermissionActivity.this, "权限" + Arrays.toString(permissions) + " 申请成功");
    }

    @Override
    public void onFail(String... permissions) {
        T.showShort(PermissionActivity.this, "权限" + Arrays.toString(permissions) + " 申请失败");
    }

}
