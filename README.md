# MVVM
这是一个android MVVM 框架，基于谷歌dataBinding技术实现。dataBinding 实现的 V 和 VM的关联；使用IOC架构实现了 M 和 V的关联。</br>
框架具有以下功能：</br>
- [业务逻辑层的分离](#业务逻辑层的分离)
- [封装了android 6.0权限申请，在申请权限时，能像View一样设置事件监听](#权限使用)
- [创建Fragment、Dialog、popupwindow都将变得极其简单](#AbsPopupwindow)
- [错误填充布局实现将变得极其简单](#错误填充布局注入)
- [具有dataBinding的一切功能](#使用)
- [封装了Okhttp网络请求，实现二级缓存，实现了网络回调监听](#网络请求)


目前该框架已经运用于公司的两个项目上，暂时没发现什么特别大的bug。</br>
如果你觉得我的代码对你有帮助，请麻烦你在右上角给我一个star.`^_^`

## 下载
[![Download](https://api.bintray.com/packages/arialyy/maven/MvvmFrame/images/download.svg)](https://bintray.com/arialyy/maven/MvvmFrame/_latestVersion)</br>
compile 'com.arialyy.frame:MVVM2:2.0.1_dev'

## 使用
* 在build.gradle 打开dataBinding 选项
```gradle
android{
    ...
    dataBinding {
        enabled = true
    }
}
```

* 在Application注册框架
```java
public class BaseApplication extends AbsApplication {
    @Override
   public void onCreate() {
       super.onCreate();
       MVMVFrame.init(this);
       //如果你需要打开异常捕获模块，去掉下面语句的注释，将两个参数改为你的接口和key，便可以将崩溃信息上传到后台服务器
       //MVMVFrame.init(this).openCrashHandler("http://192.168.2.183/server.php", "params");
   }
}
```

## Module使用
![Module使用](https://github.com/AriaLyy/MVVM/blob/master/img/mvvm.gif)

## android 6.0 权限
![android 6.0权限使用](https://github.com/AriaLyy/MVVM/blob/master/img/permission.gif)

## 业务逻辑层的分离
框架是基于谷歌dataBinding的，布局里面动态设置数据的方法为谷歌 dataBinding的用法方法，想了解更多，可参考[谷歌官方文档](http://developer.android.com/intl/zh-cn/tools/data-binding/guide.html)</br>
这是一个使用网络请求来解析本地IP的例子，请求网络的业务逻辑将写在Moudule里面，Activity只需要调用Module只负责调用Module的业务逻辑以及接受Module的回调数据，不会执行任何与业务逻辑相关的操作</br>
* 首先，为Activity创建一个布局
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
* 创建一个用来处理业务逻辑的Module
Module用来处理业务逻辑，比如网络操作，数据库操作，其它数据的获取等操作；这些都应该放在Module里面处理，这样，以后即使有新的需求发生修改，只要数据结构不发生改变，Module里面业务逻辑的任何操作都不会改变AbsActivity等组件原有的逻辑。</br>
由于Module和框架里面的其它组件都是采用聚合的方式进行实现，因此，Moudle是一个低耦合的模块，一个Module，可以被多个不同的组件调用，达到了一处业务实现，可多处被调用的目的。</br>
框架里面可以调用Module的组件有：AbsActivity、AbsFragment、AbsAlertDialog、AbsDialog、AbsDialogFragment、AbsPopupFragment。这些组件都可以在任意地方调用Module的业务逻辑。
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
这是一个获取IP信息的Module，`getIpInfo`实现了网络解析本地IP信息的业务逻辑，在其处理完业务逻辑后，通过`callback(1, "你的IP地址是：" + country + " " + province + " " + city);`方法将数据回调给调用IpModule的组件。

* AbsActivity
AbsActivity 继承于AppCompatActivity，该AbsActivity支持dataBinding的数据绑定操作，同时具有Module功能</br>
通过`getModule(IPModule.class).getIpInfo();`方式，请求了一个类名为`IPModule`的Module执行`getIpInfo()`的方法里面的业务逻辑。</br>
通过重写`protected void dataCallback(int result, Object data)`方法，将可以接收到Module执行完成业务逻辑后的回调数据,
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
上面的代码在`init(Bundle savedInstanceState)`请求`IPModule`执行了本地IP地址网络解析的操作，在`dataCallback(int result, Object data)`接收`IPModule`回调的数据，并将该数据通过dataBinding绑定到视图UI。

* dialog布局
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
* AbsDialogFragment
`AbsDialogFragment`和`AbsActivity`同样的逻辑，都是可以调用Module，也支持dataBinding绑定数据操作。</br>
使用`AbsDialogFragment`创建`DialogFragment`，你能像创建Activity一样简单，很容易创建一个完全自定义的`DialogFragment`对话框。</br>
重写`setLayoutId()`方法的`return` 为你的dialog布局ID，便能实现布局的加载。</br>
调用`getSimpleModule().onDialog(int result, Object data)`方法，你能将dialog的数据回调给生成它的组件（例如Activity或Fragment）</br>
通过重写`protected void dataCallback(int result, Object data)`方法，将可以接收到Module执行完成业务逻辑后的回调数据。
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
上面的代码在`init(Bundle savedInstanceState)`请求`IPModule`执行了本地IP地址网络解析的操作，在`dataCallback(int result, Object data)`接收`IPModule`回调的数据，并将该数据通过dataBinding绑定到视图UI。</br>
当点击确认按钮时，通过调用`getSimplerModule().onDialog(2, "对话框确认")`，`MainActivity`的将可以在`dataCallback(int result, Object data)`方法里面接收到`对话框确认`这个字符串。

## AbsPopupwindow
`popupwindow`和`AbsActivity`同样的逻辑，都是可以调用Module，也支持dataBinding绑定数据操作。</br>
使用`AbsPopupwindow`创建`popupwindow`，你能像创建Activity一样简单，很容易创建一个完全自定义的`popupwindow`</br>
重写`setLayoutId()`方法的`return` 为你的dialog布局ID，便能实现布局的加载。</br>
调用`getSimpleModule().onDialog(int result, Object data)`方法，你能将dialog的数据回调给生成它的组件（例如Activity或Fragment）</br>
通过重写`protected void dataCallback(int result, Object data)`方法，将可以接收到Module执行完成业务逻辑后的回调数据。</br>
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

## AbsFragment
`fragment`和`AbsActivity`同样的逻辑，都是可以调用Module，也支持dataBinding绑定数据操作。</br>
使用`AbsFragment`创建`fragment`，你能像创建Activity一样简单，很容易创建一个`fragment`</br>
重写`setLayoutId()`方法的`return` 为你的dialog布局ID，便能实现布局的加载。</br>
重写`protected void dataCallback(int result, Object data)`方法，将可以接收到Module执行完成业务逻辑后的回调数据。</br>
重写`onDelayLoad()`方法，你将能对某些逻辑实现延时操作，如: 在ViewPager里面，只有当Fragment处于可视状态时，Fragment才会执行`onDelayLoad()的代码`，使用延时技术，能在很大程度上优化app的性能(只有在需要的时候才加载需要的数据)。
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
        //你需要延时操作的代码
        ...
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

## 权限使用
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
## 占位布局
框架在AbsActivity、AbsFragment里面集成了错误填充布局，`你不需要在你的xml布局里面增加任何新代码`，只需要在继承与AbsActivity或者AbsFragment的Activty、fragment中调用`showTempView(int type)`方法，便能加载一个错误填充布局。</br>
调用`hintTempView()`来关闭错误填充布局。</br>
如果你需要响应布局的控件的事件，你将需要在Activty、fragment里面重写`public void onBtTempClick(View view, int type)`方法，在里面执行你的逻辑。
目前框架有3种type可以使用
```
1. type = ITempView.ERROR;      ==> 错误的填充布局
2. type = ITempView.DATA_NULL;  ==> 数据为空的填充布局
3. type = ITempView.LOADING;    ==> 加载等待的填充布局
```

### 自定义填充布局
如果你对框架自带占位布局不满意，你也可以自定义自己的占位布局。
1. 创建一个对象继承于AbsTempView
2. 重写`protected int setLayoutId()`方法的`return` 为你的dialog布局ID，便能实现布局的加载。
3. 重写`protected void init()`，在里面进行初始化操作。
4. 重写`public void onError()`在该方法里编写`type == ITempView.ERROR`的业务逻辑。
5. 重写`public void onNull()`在该方法里编写`type = ITempView.DATA_NULL`的业务逻辑。
6. 重写`public void onLoading()`在该方法里面编写`type = ITempView.LOADING`的业务逻辑。
7. 在继承于AbsActivity或者AbsFragment的组件里面调用`setCustomTempView(AbsTempView tempView)`，tempView便是你创建的继承于AbsTempView的子类。
**上面的4、5、6根据你的需求，进行选择。**</br>
注意，如果你的占位布局有控件事件，你将需要在控件的事件代码里面调用`onTempBtClick(v, mType);`方法，这样，你才能将控件的事件传递到Activity或者Fragment，如下所示</br>
```java
mBt.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
        //将事件传递到Activity或者Fragment
        onTempBtClick(v, mType);
    }
});
```
例子：
```java
public class CustomTempView extends AbsTempView {
    @InjectView(R.id.bt) Button mBt;
    @InjectView(R.id.error_temp) LinearLayout mErrorTemp;
    @InjectView(R.id.img) ImageView mErrorImg;
    @InjectView(R.id.text) TextView mErrorText;
    @InjectView(R.id.loading) ProgressBar mpb;

    public CustomTempView(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        mBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTempBtClick(v, mType);
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_custom_view;
    }

    @Override
    public void setType(int type) {
        super.setType(type);
        mpb.setVisibility(type == ITempView.LOADING ? VISIBLE : GONE);
        mErrorTemp.setVisibility(type == ITempView.LOADING ? GONE : VISIBLE);
    }

    /**
     * 处理type 为 error 时，tempView的页面逻辑
     */
    @Override
    public void onError() {
        mErrorText.setText("错误时的提示文本");
        mBt.setText("error");
    }

    /**
     * 处理type 为 null 时，tempView的页面逻辑
     */
    @Override
    public void onNull() {
        mErrorText.setText("数据为空时的提示文本");
        mBt.setText("null");
    }

    /**
     * 处理type 为 loading 时，tempView的页面逻辑
     */
    @Override
    public void onLoading() {
        //这里使用的是progress，如果使用动画，可以在这实现
    }
}
```


## 网络请求
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
