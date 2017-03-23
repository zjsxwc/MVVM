package com.example.arial.mvvm.base;

import com.arialyy.frame.core.AbsFrame;

/**
 * Created by lyy on 2016/4/1.
 */
public class Application extends android.app.Application {
  @Override public void onCreate() {
    super.onCreate();
    //        AbsFrame.init(this).openCrashHandler();
    //        AbsFrame.init(this).openCrashHandler("http://192.168.2.183/server.php", "params");
    AbsFrame.init(this);
  }
}
