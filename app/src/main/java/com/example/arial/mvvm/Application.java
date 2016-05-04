package com.example.arial.mvvm;


import com.arialyy.frame.core.MVVMFrame;

/**
 * Created by lyy on 2016/4/1.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        MVVMFrame.init(this).openCrashHandler();
//        MVVMFrame.init(this).openCrashHandler("http://192.168.2.183/server.php", "params");
        MVVMFrame.init(this);
    }
}
