package com.example.arial.mvvm.tempview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.temp.ITempView;
import com.arialyy.frame.util.show.T;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.databinding.ActivityTempViewBinding;

/**
 * Created by lyy on 2016/4/27.
 */
public class TempViewActivity extends AbsActivity<ActivityTempViewBinding> {

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
//        setUseTempView(false);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_temp_view;
    }

    @Override
    protected void dataCallback(int result, Object data) {

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
            case R.id.ft:
                startActivity(new Intent(this, TVFContentActivity.class));
                break;
        }
    }

    @Override
    public void onBtTempClick(int type) {
        super.onBtTempClick(type);
        hintTempView();
    }
}
