package com.example.arial.mvvm.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arialyy.frame.core.AbsDialogFragment;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.config.Constance;
import com.example.arial.mvvm.databinding.DialogShowBinding;
import com.example.arial.mvvm.module.IPModule;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/4/6.
 */
@SuppressLint("ValidFragment")
public class IpDialog extends AbsDialogFragment<DialogShowBinding> implements View.OnClickListener {
    @InjectView(R.id.enter) Button mEnter;
    @InjectView(R.id.cancel) Button mCancel;

    public IpDialog(Object obj) {
        super(obj);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getModule(IPModule.class).getIpInfo();
        mEnter.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_abs;
    }

    @Override
    protected void dataCallback(int result, Object data) {
        getBinding().setStr(data + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter:
                getSimplerModule().onDialog(Constance.KEY.IP_DIALOG, "对话框确认");
                break;
            case R.id.cancel:
                getSimplerModule().onDialog(Constance.KEY.IP_DIALOG, "对话框取消");
                break;
        }
        dismiss();
    }
}
