package com.jalen.screenrecord;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by joyce on 2015/10/23.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(this, "900011116", false);
    }
}
