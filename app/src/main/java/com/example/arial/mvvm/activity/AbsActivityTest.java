package com.example.arial.mvvm.activity;

import android.support.v7.widget.RecyclerView;

import com.example.arial.mvvm.R;
import com.example.arial.mvvm.base.BaseActivity;
import com.example.arial.mvvm.databinding.ActivityAbsBinding;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/7/14.
 */
public class AbsActivityTest extends BaseActivity<ActivityAbsBinding> {
    @InjectView(R.id.list)
    RecyclerView mList;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_abs;
    }
}
