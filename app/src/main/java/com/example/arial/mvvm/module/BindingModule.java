package com.example.arial.mvvm.module;

import android.content.Context;

import com.arialyy.frame.http.HttpUtil;
import com.example.arial.mvvm.base.BaseModule;
import com.example.arial.mvvm.databinding.ActivityAbsBinding;
import com.example.arial.mvvm.databinding.DialogAbsBinding;
import com.example.arial.mvvm.databinding.FragmentModuleBinding;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lyy on 2016/9/16.
 */
public class BindingModule extends BaseModule {
  private static final String IP_URL =
      "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";

  public BindingModule(Context context) {
    super(context);
  }

  /**
   * Activity binding测试
   */
  public void activityBindingTest() {
    HttpUtil util = HttpUtil.getInstance(getContext());
    util.get(IP_URL, new HttpUtil.AbsResponse() {
      @Override public void onResponse(String data) {
        super.onResponse(data);
        try {
          JSONObject obj = new JSONObject(data);
          String country = obj.getString("country");
          String province = obj.getString("province");
          String city = obj.getString("city");
          String str =
              "现在是在Module中调用binding显示数据！！\n你的IP地址是：" + country + " " + province + " " + city;
          // TODO: 2016/9/16 这里需要你进行Binding的指定，现在可以直接在module里面处理显示了
          getBinding(ActivityAbsBinding.class).setModuleStr(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(Object error) {
        super.onError(error);
      }
    });
  }

  public void fragmentBindingTest() {
    getBinding(FragmentModuleBinding.class).setModuleStr("fragment module binding测试");
  }

  public void dialogBindingTest() {
    getBinding(DialogAbsBinding.class).setStr("dialog binding 测试");
  }
}
