package com.example.arial.mvvm.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.temp.ITempView;
import com.arialyy.frame.util.show.T;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.base.BaseActivity;
import com.example.arial.mvvm.databinding.ActivityTempViewBinding;
import com.example.arial.mvvm.tempview.CustomTempView;

/**
 * Created by lyy on 2016/4/27.
 */
public class TempViewActivity extends BaseActivity<ActivityTempViewBinding> {
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle("Activity 填充界面测试");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_temp_view;
    }

    public void onClick(View view) {
        switch (view.getId()) {
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
                setCustomTempView(new CustomTempView(this));
                T.showShort(this, "自定义填充对话框设置成功");
                break;
//            case R.id.ft:
//                startActivity(new Intent(this, TVFContentActivity.class));
//                break;
//            case R.id.vp_ft:
//                startActivity(new Intent(this, TVVPContentActivity.class));
//                break;
        }
    }

    @Override
    public void onBtTempClick(View view, int type) {
        super.onBtTempClick(view, type);
        hintTempView();
    }
}
