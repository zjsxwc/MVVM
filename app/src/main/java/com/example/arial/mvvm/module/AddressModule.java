package com.example.arial.mvvm.module;

import android.content.Context;

import com.example.arial.mvvm.base.BaseModule;
import com.example.arial.mvvm.config.Constance;

/**
 * Created by lyy on 2016/7/13.
 */
public class AddressModule extends BaseModule{
    public AddressModule(Context context) {
        super(context);
    }

    public void getAddr(){
        callback(Constance.KEY.GET_ADDR, "地址：北京");
    }

}
