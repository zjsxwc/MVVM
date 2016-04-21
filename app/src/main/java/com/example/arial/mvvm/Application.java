package com.example.arial.mvvm;


import com.arialyy.frame.core.ApplicationManager;

/**
 * Created by lyy on 2016/4/1.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        ApplicationManager.init(this).openCrashHandler();
        ApplicationManager.init(this).openCrashHandler("http://127.0.0.1:2000", "jj");
    }
}
