package com.example.arial.mvvm.fragment;

import android.os.Bundle;

import com.example.arial.mvvm.R;
import com.example.arial.mvvm.base.BaseFragment;
import com.example.arial.mvvm.databinding.FragmentAbsBinding;

/**
 * Created by lyy on 2016/7/14.
 */
public class AbsFragmentTest extends BaseFragment<FragmentAbsBinding> {

    public static AbsFragmentTest newInstance() {

        Bundle args = new Bundle();

        AbsFragmentTest fragment = new AbsFragmentTest();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_abs;
    }
}
