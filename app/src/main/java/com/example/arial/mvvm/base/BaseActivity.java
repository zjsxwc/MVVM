package com.example.arial.mvvm.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.arialyy.frame.core.AbsActivity;
import com.example.arial.mvvm.R;

/**
 * Created by lyy on 2016/7/13.
 */
public abstract class BaseActivity<VB extends ViewDataBinding> extends AbsActivity<VB> {

  Toolbar mBar;

  @Override protected void init(Bundle savedInstanceState) {
    super.init(savedInstanceState);
    mBar = (Toolbar) findViewById(R.id.toolbar);
    if (mBar != null) {
      setSupportActionBar(mBar);
    }
  }

  @Override protected void dataCallback(int result, Object data) {

  }
}
