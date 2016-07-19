package com.example.arial.mvvm.tempview;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;

import com.arialyy.frame.core.AbsFragment;
import com.arialyy.frame.temp.ITempView;
import com.arialyy.frame.util.show.T;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.databinding.FragmentTempView1Binding;

/**
 * Created by lyy on 2016/4/28.
 */
@SuppressLint("ValidFragment")
public class TempViewFragment1 extends AbsFragment<FragmentTempView1Binding> {

    int mColor = Color.WHITE;

    public static TempViewFragment1 newInstance(@ColorInt int color) {
        return new TempViewFragment1(color);
    }

    private TempViewFragment1(int color) {
        mColor = color;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void onDelayLoad() {
        showTempView(ITempView.LOADING);
        //模拟延时操作
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hintTempView();
                if (getView() != null) {
                    getView().setBackgroundColor(mColor);
                    getBinding().setStr("我是fragment");
                }
            }
        }, 1000);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_temp_view_1;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
