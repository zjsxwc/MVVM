package com.example.arial.mvvm.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.arial.mvvm.R;
import com.example.arial.mvvm.base.BaseFragment;
import com.example.arial.mvvm.databinding.FragmentAbsBinding;

import butterknife.Bind;

/**
 * Created by lyy on 2016/7/14.
 */
public class AbsDialogFragmentTest extends BaseFragment<FragmentAbsBinding> {
    @Bind(R.id.list)
    RecyclerView mList;

    public static AbsDialogFragmentTest newInstance() {
        
        Bundle args = new Bundle();
        
        AbsDialogFragmentTest fragment = new AbsDialogFragmentTest();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_abs;
    }
}
