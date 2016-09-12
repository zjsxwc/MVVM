# MVVM
这是一个android IOC + MVVM的开发框架，MVVM部分来自于谷歌dataBinding技术；IOC架构实现了 M 和 V的关联。</br>
框架具有以下特点：</br>
- [业务逻辑层的分离](#业务逻辑层使用)
- [封装了android 6.0权限申请，在申请权限时，能像View一样设置事件监听](#权限使用)
- [创建Fragment、Dialog、popupwindow都将变得极其简单](#Dialog、popupwindow的getSimplerModule使用)
- [占位布局实现将变得极其简单](#占位布局)
- [具有dataBinding的一切功能](#使用)
- [封装了Okhttp网络请求，实现二级缓存，实现了网络回调监听](#网络请求)

如果你觉得我的代码对你有帮助，请麻烦你在右上角给我一个star.`^_^`

## lib
[![Download](https://api.bintray.com/packages/arialyy/maven/MvvmFrame/images/download.svg)](https://bintray.com/arialyy/maven/MvvmFrame/_latestVersion)</br>
compile 'com.jakewharton:butterknife:6.1.0'</br>
compile 'com.google.code.gson:gson:2.7'</br>
compile 'com.squareup.okhttp3:okhttp:3.2.0'</br>
compile 'com.arialyy.frame:MVVM2:2.1.0'</br>

## 用例
![img](https://github.com/AriaLyy/MVVM/blob/v_2.1/img/AbsFrameImg.gif)

## 使用
* 1、在build.gradle 打开dataBinding 选项
```gradle
android{
    ...
    dataBinding {
        enabled = true
    }
}
```

* 2、在Application注册框架
```java
public class BaseApplication extends AbsApplication {
    @Override
   public void onCreate() {
       super.onCreate();
       AbsFrame.init(this);
       //如果你需要打开异常捕获模块，去掉下面语句的注释，将两个参数改为你的接口和key，便可以将崩溃信息上传到后台服务器
       //AbsFrame.init(this).openCrashHandler("http://192.168.2.183/server.php", "params");
   }
}
```
* 3、注意事项：
框架是基于谷歌dataBinding的，布局需要遵循dataBinding的使用原则，dataBinding使用可参考[谷歌官方文档](http://developer.android.com/intl/zh-cn/tools/data-binding/guide.html)</br>

## AbsActivity使用
1、AbsActivity的使用需要ViewDataBinding支持，而ViewDataBinding是自动生成的，如下所示，系统将会自动生成一个名为：ActivityMainBinding的java文件。</br>
```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- 在layout包裹段里面编写布局文件，系统将自动生成ViewDataBinding -->
    <!-- 目前只有AbsActivity、AbsFragment、AbsDialogFragment的布局文件需要进行layou包裹处理，其它组件不需要 -->
    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:id="@+id/msg"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="hello world" />
    </RelativeLayout>
</layout>
```
2、为了实现数据分离，需要创建一个Module专门用来处理数据或者业务逻辑，如下：
```java
public class IPModule extends AbsModule {
    public IPModule(Context context) {
        super(context);
    }

    /**
     * 获取IP信息
     */
    public void getIpInfo() {
        //经常I数据或业务逻辑处理操作
        ...
        //处理完毕后，使用callback将数据回调给AbsActivity
        callback(Constance.KEY.GET_IP, "你的IP地址是：" + country + " " + province + " " + city);
    }
}
```
**ps：Module一处生成可被处处使用，如：一个业务逻辑只需要生成一次，便可以被多处不同的地方使用该逻辑**

3、生成该文件后，便可以创建Activity，如：MainActivity
```java
//ActivityMainBinding 为上面xml布局自动生成的ViewDataBinding
public class MainActivity extends AbsActivity<ActivityMainBinding>{
    //重写setLayoutId方法，return 相应的布局
    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        //在这里处理你的初始化操作

        //请求Module处理数据，这里为向IPModule请求执行了getIpInfo的操作
        getModule(IPModule.class, new AbsModule.OnCallback() {
            @Override
            public void onSuccess(int result, Object success) {
                if (result == Constance.KEY.GET_IP) {
                    T.showShort(getContext(), success + "");
                }
            }

            @Override
            public void onError(int key, Object error) {

            }
        }).getIpInfo();
    }
}
```
现在，目前支持业务逻辑分离的组件有：
AbsActivity、AbsFragment、AbsDialog、AbsDialogFragment、AbsAlertDialog、AbsPopupWindow</br>

## Dialog、popupwindow的getSimplerModule使用
在Dialog、popupwindow中调用getSimplerModule().onDialog(int. Object)方法，将可以将Object回调给生成该Dialog、popupwindow的类。
```java
public class ShowDialog extends AbsDialogFragment<DialogShowBinding> implements View.OnClickListener {

    public ShowDialog(Object obj) {
        super(obj);
    }
    //.....一些代码
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter:
                //将数据回调给生成Dialog的类
                getSimplerModule().onDialog(Constance.KEY.SHOW_DIALOG, "对话框确认");
                break;
            case R.id.cancel:
                getSimplerModule().onDialog(Constance.KEY.SHOW_DIALOG, "对话框取消");
                break;
        }
        dismiss();
    }
}
```
生成ShowDialog的类
```java
public class MainActivity extends AbsActivity<ActivityMainBinding>{
    //其它逻辑..
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        //在这里处理你的初始化操作
        //生成ShowDialog
        ShowDialog dialog = new ShowDialog(this);
        dialog.show(getSupportFragmentManager(), "ip_dialog");
    }
    @Override
    protected void dataCallback(int result, Object data) {
         if (result == Constance.KEY.SHOW_DIALOG) {
             //处理ShowDialog调用getSimplerModule().onDialog(int. Object)方法的数据
         }
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

### 自定义占位布局
如果你对框架自带占位布局不满意，你也可以自定义自己的占位布局。
```
1. 创建一个对象继承于AbsTempView
2. 重写`protected int setLayoutId()`方法的`return` 为你的dialog布局ID，便能实现布局的加载。
3. 重写`protected void init()`，在里面进行初始化操作。
4. 重写`public void onError()`在该方法里编写`type == ITempView.ERROR`的业务逻辑。
5. 重写`public void onNull()`在该方法里编写`type = ITempView.DATA_NULL`的业务逻辑。
6. 重写`public void onLoading()`在该方法里面编写`type = ITempView.LOADING`的业务逻辑。
7. 在继承于AbsActivity或者AbsFragment的组件里面调用`setCustomTempView(AbsTempView tempView)`，tempView便是你创建的继承于AbsTempView的子类。
```
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

# 混淆配置
```
-dontwarn android.support.**
-keep class android.support.**{*;}
-keep class com.arialyy.frame.**{*;}
-dontwarn com.arialyy.frame.**
-keepclassmembers class * extends com.arialyy.frame.module.AbsModule{
    public <init>(android.content.Context);
}
-keepclassmembers class * extends com.arialyy.frame.core.AbsActivity{
    protected void dataCallback(int, java.lang.Object);
}
-keepclassmembers class * extends com.arialyy.frame.core.AbsPopupWindow{
    protected void dataCallback(int, java.lang.Object);
}
-keepclassmembers class * extends com.arialyy.frame.core.AbsFragment{
    protected void dataCallback(int, java.lang.Object);
}
-keepclassmembers class * extends com.arialyy.frame.core.AbsDialogFragment{
    protected void dataCallback(int, java.lang.Object);
}
-keepclassmembers class * extends com.arialyy.frame.core.AbsAlertDialog{
    protected void dataCallback(int, java.lang.Object);
}

-keepclassmembers class * extends com.arialyy.frame.core.AbsDialog{
    protected void dataCallback(int, java.lang.Object);
}
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
