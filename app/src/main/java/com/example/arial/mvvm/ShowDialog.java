package com.example.arial.mvvm;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arialyy.frame.core.AbsDialogFragment;
import com.example.arial.mvvm.databinding.DialogShowBinding;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/4/6.
 */
@SuppressLint("ValidFragment")
public class ShowDialog extends AbsDialogFragment<DialogShowBinding> implements View.OnClickListener {
    @InjectView(R.id.enter) Button mEnter;
    @InjectView(R.id.cancel) Button mCancel;

    public ShowDialog(Object obj) {
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
        return R.layout.dialog_show;
    }

    @Override
    protected void dataCallback(int result, Object data) {
        getBinding().setStr(data + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter:
                getSimplerModule().onDialog(2, "对话框确认");
                break;
            case R.id.cancel:
                getSimplerModule().onDialog(2, "对话框取消");
                break;
        }
        dismiss();
    }
}
