package com.example.arial.mvvm;


import com.arialyy.frame.core.MVMVFrame;

/**
 * Created by lyy on 2016/4/1.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        MVMVFrame.init(this).openCrashHandler();
//        MVMVFrame.init(this).openCrashHandler("http://192.168.2.183/server.php", "params");
        MVMVFrame.init(this);
    }
}
