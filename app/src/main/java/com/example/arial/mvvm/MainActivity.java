package com.example.arial.mvvm;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import com.arialyy.frame.core.AbsActivity;
import com.example.arial.mvvm.databinding.ActivityMainBinding;
import com.example.arial.mvvm.permission.PermissionActivity;

/**
 * Created by lyy on 2016/4/12.
 */
public class MainActivity extends AbsActivity<ActivityMainBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.base:
                intent = new Intent(this, ModuleActivity.class);
                break;
            case R.id.permission:
                intent = new Intent(this, PermissionActivity.class);
                break;
            case R.id.popupwindow:
                TestPopupwindow p = new TestPopupwindow(this);
                p.showAtLocation(mRootView, Gravity.NO_GRAVITY, 0, 0);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void dataCallback(int result, Object data) {

    }
}
