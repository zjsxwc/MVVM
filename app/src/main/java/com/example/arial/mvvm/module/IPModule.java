package com.example.arial.mvvm.module;

import android.content.Context;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.module.AbsModule;
import com.example.arial.mvvm.config.Constance;
import com.example.arial.mvvm.databinding.ActivityAbsBinding;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lyy on 2016/4/6.
 */
public class IPModule extends AbsModule {
    private static final String IP_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";

    public IPModule(Context context) {
        super(context);
    }

    /**
     * 获取IP信息
     */
    public void getIpInfo() {
        HttpUtil util = HttpUtil.getInstance(getContext());
        util.get(IP_URL, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    String country = obj.getString("country");
                    String province = obj.getString("province");
                    String city = obj.getString("city");
                    //进行数据回调
                    callback(Constance.KEY.GET_IP, "你的IP地址是：" + country + " " + province + " " + city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
            }
        });
    }

}
