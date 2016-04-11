package com.example.arial.mvvm;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.permission.OnPermissionCallback;
import com.arialyy.frame.permission.PermissionManager;
import com.arialyy.frame.util.show.T;
import com.example.arial.mvvm.databinding.ActivityMainBinding;

import java.util.Arrays;

import butterknife.InjectView;

public class MainActivity extends AbsActivity<ActivityMainBinding> implements OnPermissionCallback {
    @InjectView(R.id.content)
    FrameLayout mContent;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_dialog:
                ShowDialog dialog = new ShowDialog(this);
                dialog.show(getSupportFragmentManager(), "testDialog");
                break;
            case R.id.get_ip_info:
                getModule(IPModule.class).getIpInfo();
                break;
            case R.id.permission:
                PermissionManager.getInstance().requestPermission(this, this, Manifest.permission.CAMERA);
                break;
            case R.id.permissions:
                PermissionManager.getInstance().requestPermission(this, this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE});
                break;
            case R.id.show_fragment:
//                mContent.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void dataCallback(int resultCode, Object data) {
        if (resultCode == 1) {
            getBinding().setStr(data + "");
        } else if (resultCode == 2) {
            getBinding().setDialogStr(data + "");
        }
    }

    @Override
    public void onSuccess(String... permissions) {
        T.showShort(MainActivity.this, "权限" + Arrays.toString(permissions) + " 申请成功");
    }

    @Override
    public void onFail(String... permissions) {
        T.showShort(MainActivity.this, "权限" + Arrays.toString(permissions) + " 申请失败");
    }
}
