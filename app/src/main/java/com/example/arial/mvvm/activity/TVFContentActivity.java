package com.example.arial.mvvm.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.arialyy.frame.core.AbsActivity;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.base.BaseActivity;
import com.example.arial.mvvm.databinding.ActivityTempViewFgContentBinding;

/**
 * Created by lyy on 2016/4/27.
 */
public class TVFContentActivity extends BaseActivity<ActivityTempViewFgContentBinding> {
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle("Fragment填充界面测试");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_temp_view_fg_content;
    }

}
