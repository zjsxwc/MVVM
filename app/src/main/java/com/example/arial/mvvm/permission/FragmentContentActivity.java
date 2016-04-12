package com.example.arial.mvvm.permission;

import com.arialyy.frame.core.AbsActivity;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.databinding.ActivityFragmentContentBinding;

/**
 * Created by lyy on 2016/4/12.
 */
public class FragmentContentActivity extends AbsActivity<ActivityFragmentContentBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_fragment_content;
    }

    @Override
    protected void dataCallback(int result, Object data) {

    }
}
