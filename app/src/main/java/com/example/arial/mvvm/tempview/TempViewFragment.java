package com.example.arial.mvvm.tempview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arialyy.frame.core.AbsFragment;
import com.arialyy.frame.temp.ITempView;
import com.arialyy.frame.util.show.T;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.databinding.FragmentTempViewBinding;
import com.example.arial.mvvm.databinding.LayoutTempViewBinding;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/4/27.
 */
public class TempViewFragment extends AbsFragment<FragmentTempViewBinding> implements View.OnClickListener {
    @InjectView(R.id.net_error)
    Button mError;

    @Override
    protected void init(Bundle savedInstanceState) {
        mError.setOnClickListener(this);
    }

    @Override
    protected void onDelayLoad() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_temp_view;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.net_error:
                showTempView(ITempView.ERROR);
                break;
            case R.id.data_null:
                showTempView(ITempView.DATA_NULL);
                break;
            case R.id.loading:
                showTempView(ITempView.LOADING);
                hintTempView(2000);
                break;
            case R.id.bind_test:
                getBinding().setStr("test");
                break;
            case R.id.custom_temp:
                setCustomTempView(new CustomTempView(getContext()));
                T.showShort(getContext(), "自定义填充对话框设置成功");
                break;
            case R.id.ft:
                startActivity(new Intent(getContext(), TVFContentActivity.class));
                break;
        }
    }
}
