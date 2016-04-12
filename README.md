# MVVM
这是一个android MVVM 框架，基于谷歌dataBinding技术实现。dataBinding 实现的 V 和 VM的关联；使用IOC架构实现了 M 和 VM的关联。</br>
框架具有以下功能：</br>
* 业务逻辑层的分离。
* 封装了android 6.0权限申请，在申请权限时，能像View一样设置事件监听。
* 具有dataBinding的一切功能。
* 封装了Okhttp网络请求，实现二级缓存，实现了网络回调监听。

目前该框架已经运用于公司的两个项目上，暂时没发现什么特别大的bug。</br>
如果你觉得我的代码对你有帮助，请麻烦你在右上角给我一个star.^_^

# 下载
[![Download](https://api.bintray.com/packages/arialyy/maven/MvvmFrame/images/download.svg)](https://bintray.com/arialyy/maven/MvvmFrame/_latestVersion)</br>
compile 'com.arialyy.frame:MVVM2:1.0.7'
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

# 创建一个IP信息获取例子
框架是基于谷歌dataBinding，布局里面动态设置数据的方法为谷歌 dataBinding的用法方法，想了解更多，可参考[谷歌官方文档](http://developer.android.com/intl/zh-cn/tools/data-binding/guide.html)</br>
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
Module里面用来处理一般的业务逻辑，比如网络操作，数据库操作，其它数据的获取等操作；这些都应该放在Module里面处理。</br>
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
这就是Activity的所有代码，只需要简单的两行代码便能实现一个使用网络请求数据并展示的操作。所有的业务逻辑全部封装
在一个Module里面，将来有该业务如果有新的需求，改变的也只是Module里面的代码，而不用过多修改Activity的代码。</br>
由于Module是一个低耦合的模块，你可以在所有继承于AbsActivity、AbsFragment、AbsAlertDialog、AbsDialog、
AbsDialogFragment、AbsPopupFragment等超类的子类调用同一个Module，而不需要编写重复的代码，这样可以达到代码复用的目的。
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

* AbsDialogFragment
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

# 示例
![例子](https://github.com/AriaLyy/MVVM/blob/master/img/mvvm.gif "")

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



