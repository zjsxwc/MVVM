package com.example.arial.mvvm.tempview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arialyy.frame.temp.AbsTempView;
import com.arialyy.frame.temp.ITempView;
import com.example.arial.mvvm.R;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/4/27.
 */
public class CustomTempView extends AbsTempView {
    @InjectView(R.id.bt)
    Button mBt;
    @InjectView(R.id.error_temp)
    LinearLayout mErrorTemp;
    @InjectView(R.id.img)
    ImageView mErrorImg;
    @InjectView(R.id.text)
    TextView mErrorText;
    @InjectView(R.id.loading)
    ProgressBar mpb;

    public CustomTempView(Context context) {
        super(context);
    }

    public CustomTempView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        mBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTempBtClick(mType);
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_custom_view;
    }

    @Override
    public void setType(int type) {
        super.setType(type);
        mpb.setVisibility(type == ITempView.LOADING ? VISIBLE : GONE);
        mErrorTemp.setVisibility(type == ITempView.LOADING ? GONE : VISIBLE);
    }

    /**
     * 处理type 为 error 时，tempView的页面逻辑
     */
    @Override
    public void onError() {
        mErrorText.setText("错误时的提示文本");
        mBt.setText("error");
    }

    /**
     * 处理type 为 null 时，tempView的页面逻辑
     */
    @Override
    public void onNull() {
        mErrorText.setText("数据为空时的提示文本");
        mBt.setText("null");
    }

    /**
     * 处理type 为 loading 时，tempView的页面逻辑
     */
    @Override
    public void onLoading() {
        //这里使用的是progress，如果使用动画，可以在这实现
    }
}
