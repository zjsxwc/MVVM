package com.arialyy.frame.module;

import android.content.Context;
import android.text.TextUtils;

import com.arialyy.frame.module.inf.ModuleListener;
import com.arialyy.frame.util.ObjUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by AriaLyy on 2015/2/3.
 * 抽象的module
 */
public class AbsModule {
    public String TAG = "";
    private Context        mContext;
    private ModuleListener mModuleListener;
    private Map<String, OnCallback> mPool = new HashMap<>();

    public interface OnCallback {
        public void onSuccess(int key, Object success);

        public void onError(int key, Object error);
    }

    public AbsModule(Context context) {
        mContext = context;
        init();
    }

    /**
     * 初始化一些东西
     */
    private void init() {
        String className = getClass().getName();
        String arrays[]  = className.split("\\.");
        TAG = arrays[arrays.length - 1];
    }

    public void setModuleListener(ModuleListener moduleListener) {
        if (moduleListener == null)
            throw new NullPointerException("ModuleListener不能为空");
        this.mModuleListener = moduleListener;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 注册module回调
     *
     * @param key 一个key只能对应一个callback，而一个callback可对应多个key
     */
    public AbsModule regCallback(int key, OnCallback callback) {
        if (mPool.containsValue(callback)) {
            String oldMapKey = ObjUtil.getKeyByValue(mPool, callback);
            if (!TextUtils.isEmpty(oldMapKey)){
                String[] keys = oldMapKey.split(",");
                for (String str : keys){
                    if (str.equals(key + "")){
                        return this;
                    }
                }
                String newMapKey = oldMapKey + "," + key;
                mPool.put(newMapKey, mPool.remove(oldMapKey));
            }else {
                mPool.put(key + "", callback);
            }
        } else {
            mPool.put(key + "", callback);
        }
        return this;
    }



    /**
     * 单一的module回调
     */
    protected SuperCallback callback(int key) {
        OnCallback callback = mPool.get(key);
        if (callback == null) {
            throw new NullPointerException("请注册key = " + key + "的module回调");
        }
        return new SuperCallback(key, callback);
    }

    /**
     * 统一的回调
     *
     * @param result 返回码
     * @param data   回调数据
     */
    @Deprecated
    protected void callback(final int result, final Object data) {
        mModuleListener.callback(result, data);
    }

    /**
     * module回调
     *
     * @param method 回调的方法名
     */
    @Deprecated
    protected void callback(String method) {
        mModuleListener.callback(method);
    }

    /**
     * 带参数的module回调
     *
     * @param method        回调的方法名
     * @param dataClassType 回调数据类型
     * @param data          回调数据
     */
    @Deprecated
    protected void callback(String method, Class<?> dataClassType, Object data) {
        mModuleListener.callback(method, dataClassType, data);
    }

    private static class SuperCallback {
        OnCallback callback;
        int        key;

        public SuperCallback(int key, OnCallback callback) {
            this.callback = callback;
            this.key = key;
        }

        public void onSuccess(Object success) {
            callback.onSuccess(key, success);
        }

        public void onError(Object error) {
            callback.onError(key, error);
        }
    }
}
