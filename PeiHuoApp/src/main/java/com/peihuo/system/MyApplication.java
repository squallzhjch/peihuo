package com.peihuo.system;

import android.app.Application;

import com.peihuo.thread.ThreadManager;
import com.peihuo.util.MyLogManager;
import com.tencent.bugly.crashreport.CrashReport;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        ThreadManager.getInstance().init(this);
        MyLogManager.getInstance().init(this);
        SharedConfigHelper.getInstance().init(this);
        DataDictionary.getInstance().init(this);
        CrashReport.initCrashReport(getApplicationContext(), "27b2e07773", false);
    }
}
