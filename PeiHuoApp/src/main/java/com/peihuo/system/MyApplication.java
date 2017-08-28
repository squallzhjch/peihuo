package com.peihuo.system;

import android.app.Application;

import com.peihuo.db.MySqlManager;
import com.peihuo.thread.ThreadManager;
import com.peihuo.util.LogManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        ThreadManager.getInstance().init(this);
        LogManager.getInstance().init(this);
        SharedConfigHelper.getInstance().init(this);
    }
}
