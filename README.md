# MVVM
这是一个android MVVM 框架，基于谷歌dataBinding技术实现。dataBinding 实现的 V 和 VM的关联；使用IOC架构实现了 M 和 V的关联。</br>
框架具有以下功能：</br>
- [业务逻辑层的分离](#通过一个例子来介绍框架)
- [封装了android 6.0权限申请，在申请权限时，能像View一样设置事件监听](#android 6.0 权限使用)
- [创建Fragment、Dialog、popupwindow都将变得极其简单](#创建Fragment)
- [具有dataBinding的一切功能](#使用)
- [封装了Okhttp网络请求，实现二级缓存，实现了网络回调监听](#网络请求)


目前该框架已经运用于公司的两个项目上，暂时没发现什么特别大的bug。</br>
如果你觉得我的代码对你有帮助，请麻烦你在右上角给我一个star.^_^

# 下载
[![Download](https://api.bintray.com/packages/arialyy/maven/MvvmFrame/images/download.svg)](https://bintray.com/arialyy/maven/MvvmFrame/_latestVersion)</br>
compile 'com.arialyy.frame:MVVM2:1.0.8'

# 使用
* 在build.gradle 打开dataBinding 选项
```gradle
android{
    ...
    dataBinding {
        enabled = true
    }
}
```

* 创建Application 并继承 AbsApplication
```java
public class BaseApplication extends AbsApplication {
    ...
}
```

* 修改AndroidManifest.xml文件, 指定Application对象为AbsApplication或者其子类。
```xml
<application
        ...
        android:name=".base.BaseApplication">
</application>
```

# Module使用
![Module使用](https://github.com/AriaLyy/MVVM/blob/master/img/mvvm.gif "")

# android 6.0 权限
![android 6.0权限使用](https://github.com/AriaLyy/MVVM/blob/master/img/permission.gif)

# 通过一个例子来介绍框架
这是一个使用网络请求来解析本地IP的例子</br>
框架是基于谷歌dataBinding的，布局里面动态设置数据的方法为谷歌 dataBinding的用法方法，想了解更多，可参考[谷歌官方文档](http://developer.android.com/intl/zh-cn/tools/data-binding/guide.html)</br>
* 创建一个布局
activity_main.xml
```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable name="str" type="String" />
        <variable name="dialogStr" type="String" />
    </data>

    <RelativeLayout 
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:paddingBottom="@dimen/activity_vertical_margin"
        android:paddingLeft="@dimen/activity_horizontal_margin"
        android:paddingRight="@dimen/activity_horizontal_margin"
        android:paddingTop="@dimen/activity_vertical_margin">

        <TextView
            android:id="@+id/msg"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{str}" />

        <TextView
            android:id="@+id/dialog_msg"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@+id/msg"
            android:text="@{dialogStr}" />

        <Button
            android:onClick="onClick"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_below="@+id/dialog_msg"
            android:background="?android:attr/selectableItemBackground"
            android:text="显示对话框" />
    </RelativeLayout>
</layout>
```
* 创建一个Module
Module里面用来处理业务逻辑，比如网络操作，数据库操作，其它数据的获取等操作；这些都应该放在Module里面处理。</br>
在Module里面处理业务逻辑，一个业务逻辑，可以多处使用，以达到复用的目的</br>

IPtModule.java
```java
public class IPModule extends AbsModule {
    private static final String IP_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";

    public IPModule(Context context) {
        super(context);
    }
    /**获取IP信息*/
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
                    callback(1, "你的IP地址是：" + country + " " + province + " " + city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
```

* AbsActivity
下面是Activity的所有代码，只需要简单的两行代码便能实现一个使用网络请求数据并展示的操作。所有的业务逻辑全部封装
在一个Module里面，将来有该业务如果有新的需求，改变的也只是Module里面的代码，而不用过多修改Activity的代码。</br>
由于Module是一个低耦合的模块，当你在多个地方使用同一个网络请求时，你可以在所有继承于AbsActivity、AbsFragment、AbsAlertDialog、AbsDialog、
AbsDialogFragment、AbsPopupFragment等超类的子类调用同一个Module，而不需要编写重复的代码，这样可以达到代码复用的目的。</br>
MainActivity.java
```java
public class MainActivity extends AbsActivity<ActivityMainBinding> {
    
    /**设置布局*/
    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        //调用Module请求IP信息
        getModule(IPModule.class).getIpInfo();
    }
    
    public void onClick(View view) {
        ShowDialog dialog = new ShowDialog(this);
        dialog.show(getSupportFragmentManager(), "testDialog");
    }
    
    /**接收数据回调*/
    @Override
    protected void dataCallback(int result, Object data) {
        //使用dataBinding进行数据绑定
        if (resultCode == 1) {
             getBinding().setStr(data + "");    //来自网络的数据
        } else if (resultCode == 2) {
             getBinding().setDialogStr(data + "");  //来自对话框的数据
        }
    }
}
```

* 创建Dialog
AbsDialogFragment是继承于DialogFragment 的一个对话框，同样的，该对话框也支持dataBinding数据绑定操作，也支持调用Module操作</br>
使用该对话框，继承该类，你能创建Activity一样，很容易创建一个完全自定义的Dialog对话框。</br>
通过在AbsDialogFragment的子类里面调用getSimpleModule().onDialog(int result, Object data)，你将能很容易将对话的数据传递给
生成该对话框的对象（例如Activity或Fragment）。
```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable name="str" type="String" />
    </data>

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:id="@+id/title"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:background="@color/colorPrimary"
            android:gravity="center"
            android:text="对话框标题"
            android:textColor="@android:color/white"
            android:textSize="18sp" />

        <TextView
            android:id="@+id/msg"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_below="@+id/title"
            android:gravity="center"
            android:padding="16dp"
            android:text="@{str}"
            android:textSize="16sp" />

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:layout_below="@+id/msg"
            android:orientation="horizontal">

            <Button
                android:id="@+id/enter"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_weight="1"
                android:background="?android:attr/selectableItemBackground"
                android:text="确认" />

            <Button
                android:id="@+id/cancel"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_weight="1"
                android:background="?android:attr/selectableItemBackground"
                android:text="取消" />

        </LinearLayout>
    </RelativeLayout>
</layout>
```

ShowDialog.java
```java
public class ShowDialog extends AbsDialogFragment<DialogShowBinding> implements View.OnClickListener {
    @InjectView(R.id.enter) Button mEnter;
    @InjectView(R.id.cancel) Button mCancel;

    public ShowDialog(Object obj) {
        super(obj);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //调用Module获取IP从网络获取IP信息
        getModule(IPModule.class).getIpInfo();
        mEnter.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_show;
    }

    @Override
    protected void dataCallback(int result, Object data) {
        getBinding().setStr(data + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter:
                //将数据回调给生成Dialog的类
                getSimplerModule().onDialog(2, "对话框确认");
                break;
            case R.id.cancel:
                getSimplerModule().onDialog(2, "对话框取消");
                break;
        }
        dismiss();
    }
}
```

# 创建Fragment
AbsFragment继承于Fragment，同样的，AbsFragment也支持dataBinding数据绑定操作，也支持调用Module操作</br>
同时，Fragmen支持延时操作，不需要复杂的代码，你只需要在onDelayLoad()编写你的代码，便能实现延时操作。
```java
public class PermissionFragment extends AbsFragment<FragmentTestBinding>{
    
    //在这执行Fragmen初始化操作
    @Override
    protected void init(Bundle savedInstanceState) {
        ...
    }
    
    //在这执行Fragment延时操作
    @Override
    protected void onDelayLoad() {

    }
    
    //设置布局Id
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_test;
    }
    
    //数据回调
    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
```

# 创建popupwindow
AbsPopupwindow继承于PopupWindow，只需要简单的代码，便能实现完全自定义的popupwindow，同样的，AbsPopupWindow也支持调用Module操作</br>
如下所示，你只需要编写简单的代码，便能实现一个完全自定义的popupwindow
```java
public class TestPopupwindow extends AbsPopupWindow {
    public TestPopupwindow(Context context) {
        super(context);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.popupwindow_test;
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }
}
```

# android 6.0 权限使用
框架封装了android 6.0 的权限调用，例如请求一个使用摄像头的操作，你只需要使用以下代码便能实现权限请求以及权限请求回调。
```java
PermissionManager.getInstance().requestPermission(this, new OnPermissionCallback() {
                    @Override
                    public void onSuccess(String... permissions) {
                          T.showShort(PermissionActivity.this, "权限" + Arrays.toString(permissions) + " 申请成功");
                    }

                    @Override
                    public void onFail(String... permissions) {
                          T.showShort(PermissionActivity.this, "权限" + Arrays.toString(permissions) + " 申请失败");
                    }
                }, Manifest.permission.CAMERA);
```
当同时请求多个权限时，你只需要给第三个参数传入一个权限数组
```java
PermissionManager.getInstance().requestPermission(this, new OnPermissionCallback() {
                    @Override
                    public void onSuccess(String... permissions) {
                          T.showShort(PermissionActivity.this, "权限" + Arrays.toString(permissions) + " 申请成功");
                    }

                    @Override
                    public void onFail(String... permissions) {
                          T.showShort(PermissionActivity.this, "权限" + Arrays.toString(permissions) + " 申请失败");
                    }
                }, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE});
```
除了普通权限的封装，本框架还封装了两个特殊的权限请求，分别是悬浮框权限和修改系统设置权限，使用方法和请求普通权限的方法一致
* 悬浮框权限
```java
//obj 只能是Activity、Fragment 的子类及其衍生类
PermissionManager.getInstance().requestAlertWindowPermission(Object obj, OnPermissionCallback callback);
```
* 修改系统设置的权限
```java
PermissionManager.getInstance().requestWriteSettingPermission(Object obj, OnPermissionCallback callback);
```

# 网络请求
框架封装OKHTTP实现了网络请求及其网络请求回调，使用以下代码，便能实现一个网络请求及其回调</br>
这是一个请求解析IP地址的例子
```java
//获取网络请求实例
HttpUtil util = HttpUtil.getInstance(getContext());   
util.get(IP_URL, new HttpUtil.AbsResponse() {
    //成功的数据回调
    @Override
    public void onResponse(String data) {
    super.onResponse(data);
        try {
            JSONObject obj = new JSONObject(data);
            String country = obj.getString("country");
            String province = obj.getString("province");
            String city = obj.getString("city");
        } catch (JSONException e) {
             e.printStackTrace();
        }
    }
    //失败的数据回调
    @Override
    public void onError(Object error) {
        super.onError(error);
    }
});
```

License
-------

    Copyright 2016 AriaLyy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



