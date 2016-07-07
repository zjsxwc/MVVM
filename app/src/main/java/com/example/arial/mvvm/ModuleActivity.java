package com.example.arial.mvvm;

import android.os.Bundle;
import android.view.View;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.module.AbsModule;
import com.example.arial.mvvm.databinding.ActivityModuleBinding;

public class ModuleActivity extends AbsActivity<ActivityModuleBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_module;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle("Module 使用");
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
            case R.id.get_ip_info_1:
//                getModule(IPModule.class).regCallback(1, new AbsModule.OnCallback() {
//                    @Override
//                    public void onSuccess(int key, Object success) {
//
//                    }
//
//                    @Override
//                    public void onError(int key, Object error) {
//
//                    }
//                })
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


}
