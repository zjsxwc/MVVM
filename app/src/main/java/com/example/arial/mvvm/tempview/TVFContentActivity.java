package com.example.arial.mvvm.tempview;

import com.arialyy.frame.core.AbsActivity;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.databinding.ActivityTempViewFgContentBinding;

/**
 * Created by lyy on 2016/4/27.
 */
public class TVFContentActivity extends AbsActivity<ActivityTempViewFgContentBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_temp_view_fg_content;
    }

    @Override
    protected void dataCallback(int result, Object data) {

    }
}
