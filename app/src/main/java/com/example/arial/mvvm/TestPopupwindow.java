package com.example.arial.mvvm;

import android.content.Context;

import com.arialyy.frame.core.AbsPopupWindow;

/**
 * Created by lyy on 2016/4/19.
 */
public class TestPopupwindow extends AbsPopupWindow {
    public TestPopupwindow(Context context) {
        super(context);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.popupwindow_test;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
