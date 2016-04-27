package com.arialyy.frame.core;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arialyy.frame.module.AbsModule;
import com.arialyy.frame.module.IOCProxy;
import com.arialyy.frame.permission.PermissionManager;
import com.arialyy.frame.temp.AbsTempView;
import com.arialyy.frame.temp.OnTempBtClickListener;
import com.arialyy.frame.temp.TempView;
import com.arialyy.frame.util.StringUtil;

import butterknife.ButterKnife;

/**
 * Created by lyy on 2015/11/4.
 * 基础Fragment
 */
public abstract class AbsFragment<VB extends ViewDataBinding> extends Fragment implements OnTempBtClickListener {
    protected String TAG = "";
    private VB mBind;
    private IOCProxy mProxy;
    protected View mRootView;
    protected AbsActivity mActivity;
    private ModuleFactory mModuleF;
    protected boolean isInit;
    protected AbsTempView mTempView;
    protected boolean useTempView = true;
    protected ViewGroup mParent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, setLayoutId(), container, false);
        mParent = container;
        initFragment();
        mRootView = mBind.getRoot();
        return mRootView;
    }

    private void initFragment() {
        TAG = StringUtil.getClassName(this);
        mProxy = IOCProxy.newInstance(this);
        mModuleF = ModuleFactory.newInstance();
        ButterKnife.inject(this, mBind.getRoot());
        if (useTempView) {
            mTempView = new TempView(getContext());
            mTempView.setBtListener(this);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AbsActivity) {
            mActivity = (AbsActivity) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState);
        isInit = true;
        if (getUserVisibleHint()) {
            onDelayLoad();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isInit) {
            isInit = false;
            onDelayLoad();
        }
    }

    protected abstract void init(Bundle savedInstanceState);

    /**
     * 是否使用填充界面
     *
     * @param useTempView
     */
    protected void setUseTempView(boolean useTempView) {
        this.useTempView = useTempView;
    }

    /**
     * 设置自定义的TempView
     *
     * @param tempView
     */
    protected void setCustomTempView(AbsTempView tempView) {
        mTempView = tempView;
        mTempView.setBtListener(this);
    }

    /**
     * 显示填充对话框
     *
     * @param type {@link TempView#ERROR}
     *             {@link TempView#DATA_NULL}
     *             {@link TempView#LOADING}
     */
    protected void showTempView(int type) {
        if (mTempView == null || !useTempView) {
            return;
        }
        mTempView.setVisibility(View.VISIBLE);
        mTempView.setType(type);
        mParent.removeView(mRootView);
        mParent.addView(mTempView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 关闭错误填充对话框
     */
    protected void hintTempView() {
        hintTempView(0);
    }

    /**
     * 延时关闭填充对话框
     */
    protected void hintTempView(int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTempView == null || !useTempView) {
                    return;
                }
                mTempView.clearFocus();
                mTempView.setVisibility(View.GONE);
                mParent.removeView(mTempView);
                mParent.addView(mRootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }, delay);
    }

    @Override
    public void onBtTempClick(int type) {

    }

    /**
     * 获取binding对象
     */
    protected VB getBinding() {
        return mBind;
    }

    /**
     * 获取Module
     *
     * @param clazz {@link AbsModule}
     */
    protected <M extends AbsModule> M getModule(Class<M> clazz) {
        M module = mModuleF.getModule(getContext(), clazz);
        mProxy.changeModule(module);
        return module;
    }

    /**
     * 延时加载
     */
    protected abstract void onDelayLoad();

    /**
     * 设置资源布局
     *
     * @return
     */
    protected abstract int setLayoutId();

    /**
     * 数据回调
     */
    protected abstract void dataCallback(int result, Object obj);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelp.getInstance().handlePermissionCallback(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionHelp.getInstance().handleSpecialPermissionCallback(getContext(), requestCode, resultCode, data);
    }
}
