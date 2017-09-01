package com.peihuo.system;

import android.app.Application;

import com.peihuo.thread.ThreadManager;
import com.peihuo.util.MyLogManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        ThreadManager.getInstance().init(this);
        MyLogManager.getInstance().init(this);
        SharedConfigHelper.getInstance().init(this);
    }
}
