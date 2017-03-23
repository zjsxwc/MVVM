package com.example.arial.mvvm.tempview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arialyy.frame.core.AbsFragment;
import com.arialyy.frame.temp.ITempView;
import com.arialyy.frame.util.show.T;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.databinding.FragmentTempViewBinding;

import butterknife.Bind;

/**
 * Created by lyy on 2016/4/27.
 */
public class TempViewFragment extends AbsFragment<FragmentTempViewBinding>
    implements View.OnClickListener {
  @Bind(R.id.net_error) Button mError;
  @Bind(R.id.data_null) Button mNull;
  @Bind(R.id.loading) Button mLoading;
  @Bind(R.id.bind_test) Button mBindTest;
  @Bind(R.id.custom_temp) Button mCustomBt;

  @Override protected void init(Bundle savedInstanceState) {
    mError.setOnClickListener(this);
    mNull.setOnClickListener(this);
    mLoading.setOnClickListener(this);
    mBindTest.setOnClickListener(this);
    mCustomBt.setOnClickListener(this);
  }

  @Override protected void onDelayLoad() {
  }

  @Override protected int setLayoutId() {
    return R.layout.fragment_temp_view;
  }

  @Override protected void dataCallback(int result, Object obj) {

  }

  @Override public void onBtTempClick(View view, int type) {
    super.onBtTempClick(view, type);
    hintTempView();
  }

  @Override public void onClick(View v) {
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
    }
  }
}
