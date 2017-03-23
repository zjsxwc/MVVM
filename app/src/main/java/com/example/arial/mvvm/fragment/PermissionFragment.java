package com.example.arial.mvvm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.arial.mvvm.R;
import com.example.arial.mvvm.activity.TVFContentActivity;
import com.example.arial.mvvm.activity.TVVPContentActivity;
import com.example.arial.mvvm.activity.TempViewActivity;
import com.example.arial.mvvm.base.BaseFragment;
import com.example.arial.mvvm.databinding.FragmentTestBinding;
import com.example.arial.mvvm.permission.FragmentContentActivity;
import com.example.arial.mvvm.permission.PermissionActivity;

import butterknife.Bind;

/**
 * Created by lyy on 2016/7/19.
 */
public class PermissionFragment extends BaseFragment<FragmentTestBinding>
    implements View.OnClickListener {
  @Bind(R.id.temp_activity) Button mActivityTestBt;
  @Bind(R.id.temp_fragment) Button mFragmentTestBt;

  public static PermissionFragment newInstance() {

    Bundle args = new Bundle();

    PermissionFragment fragment = new PermissionFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override protected int setLayoutId() {
    return R.layout.fragment_test_temp;
  }

  @Override protected void init(Bundle savedInstanceState) {
    super.init(savedInstanceState);
    mActivityTestBt.setOnClickListener(this);
    mFragmentTestBt.setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.temp_activity:
        startActivity(new Intent(getContext(), PermissionActivity.class));
        break;
      case R.id.temp_fragment:
        startActivity(new Intent(getContext(), FragmentContentActivity.class));
        break;
    }
  }
}
