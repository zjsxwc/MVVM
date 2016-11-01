package com.example.arial.mvvm.permission;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.temp.AbsTempView;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.base.BaseActivity;
import com.example.arial.mvvm.databinding.ActivityFragmentContentBinding;


/**
 * Created by lyy on 2016/4/12.
 */
public class FragmentContentActivity extends BaseActivity<ActivityFragmentContentBinding> {
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle("Fragment 权限测试");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_fragment_content;
    }

}
