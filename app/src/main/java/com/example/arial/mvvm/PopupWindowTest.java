package com.example.arial.mvvm;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arialyy.frame.core.AbsPopupWindow;
import com.arialyy.frame.module.AbsModule;
import com.arialyy.frame.util.show.T;
import com.example.arial.mvvm.config.Constance;
import com.example.arial.mvvm.module.AddressModule;
import com.example.arial.mvvm.module.IPModule;

import butterknife.Bind;

/**
 * Created by lyy on 2016/4/19.
 */
public class PopupWindowTest extends AbsPopupWindow implements View.OnClickListener {
    @Bind(R.id.bt)
    Button mBt;
    @Bind(R.id.module)
    Button mModuleBt;

    public PopupWindowTest(Context context, Object obj) {
        super(context, null, obj);
    }

    @Override
    protected void init() {
        super.init();
        mBt.setOnClickListener(this);
        mModuleBt.setOnClickListener(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.popupwindow_test;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                dismiss();
                getSimplerModule().onDialog(Constance.KEY.POP, "我是PopupWindow的数据回调");
                break;
            case R.id.module:
                //使用模型
                getModule(AddressModule.class, new AbsModule.OnCallback() {

                    @Override
                    public void onSuccess(int result, Object success) {
                        if (result == Constance.KEY.GET_ADDR) {
                            T.showShort(getContext(), success + "");
                        }
                    }

                    @Override
                    public void onError(int key, Object error) {

                    }
                }).getAddr();
                break;
        }
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
