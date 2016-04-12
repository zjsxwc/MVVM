package com.example.arial.mvvm.permission;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arialyy.frame.core.AbsFragment;
import com.arialyy.frame.permission.OnPermissionCallback;
import com.arialyy.frame.permission.PermissionManager;
import com.arialyy.frame.util.show.T;
import com.example.arial.mvvm.R;
import com.example.arial.mvvm.databinding.FragmentTestBinding;

import java.util.Arrays;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/4/11.
 */
public class PermissionFragment extends AbsFragment<FragmentTestBinding> implements View.OnClickListener{

    @InjectView(R.id.permission)
    Button mBt;

    @InjectView(R.id.close)
    Button mClose;

    public static PermissionFragment newInstance() {
        
        Bundle args = new Bundle();
        
        PermissionFragment fragment = new PermissionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mBt.setOnClickListener(this);
        mClose.setOnClickListener(this);
    }

    @Override
    protected void onDelayLoad() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.permission:
                PermissionManager.getInstance().requestPermission(PermissionFragment.this, new OnPermissionCallback() {
                    @Override
                    public void onSuccess(String... permissions) {
                        T.showShort(getContext(), "权限" + Arrays.toString(permissions) + " 申请成功");
                    }

                    @Override
                    public void onFail(String... permissions) {
                        T.showShort(getContext(), "权限" + Arrays.toString(permissions) + " 申请失败");
                    }
                }, Manifest.permission.CAMERA);
                break;
            case R.id.close:
                onDestroy();
                break;
        }
    }
}
