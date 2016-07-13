package com.example.arial.mvvm.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.arialyy.frame.core.AbsActivity;

/**
 * Created by lyy on 2016/7/13.
 */
public abstract class BaseActivity<VB extends ViewDataBinding> extends AbsActivity<VB> {

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    protected void dataCallback(int result, Object data) {

    }
}
