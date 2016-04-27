package com.example.arial.mvvm.tempview;

import android.os.Bundle;

import com.arialyy.frame.core.AbsFragment;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.databinding.LayoutTempViewBinding;

/**
 * Created by lyy on 2016/4/27.
 */
public class TempViewFragment extends AbsFragment<LayoutTempViewBinding> {
    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void onDelayLoad() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_temp_view;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
