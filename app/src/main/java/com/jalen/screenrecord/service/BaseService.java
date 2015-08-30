package com.jalen.screenrecord.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 服务基类
 */
public class BaseService extends Service {
    public String tag;

    public BaseService() {
        tag = this.getClass().getSimpleName();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(tag, "---------onBind ");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(tag, "---------onStartCommand ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(tag, "---------onUnbind ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(tag, "---------onDestroy ");
        super.onDestroy();
    }
}
