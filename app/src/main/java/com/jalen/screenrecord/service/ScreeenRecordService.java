package com.jalen.screenrecord.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.jalen.screenrecord.Contants;
import com.jalen.screenrecord.R;
import com.jalen.screenrecord.activity.Main;
import com.jalen.screenrecord.fragment.VideoListFragment;


/**
 * 视频录制服务
 */
public class ScreeenRecordService extends Service {
    private static final String EXTRA_RESULT_CODE = "result-code";
    private static final String EXTRA_DATA = "data";
    private static final int NOTIFICATION_ID_RECORDING = 0x0001;
    public static final String ACTION_RECEIVER = "android.intent.action.service.record";

    public static final String ACTION = "com.jalen.screenrecord.service";
    /**
     * 配置设置辅助对象
     */
    private SharedPreferences sp;
    public String tag = "ScreeenRecordService";

    RecordingSession session;

    public static Intent newIntent(Context context, int resultCode, Intent data){
        Intent intent = new Intent(context, ScreeenRecordService.class);
        intent.putExtra(EXTRA_RESULT_CODE, resultCode);
        intent.putExtra(EXTRA_DATA, data);
        return intent;
    }
    public ScreeenRecordService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(tag, "---------onBind ");
        //  在这里把屏幕录制类控制器代理给前端，让他们持有控制器
        return new ScreenRecordController();
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

    public boolean getBooleanPreferenceByKey(String key){
        sp = getSharedPreferences(Contants.APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
    public void setBooleanPreference(String key, boolean value){
        sp = getSharedPreferences(Contants.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,value);
        editor.commit();
        editor = null;
    }

    /**
     * 屏幕录制控制类
     */
    public class ScreenRecordController extends Binder {

        public void startScreenRecordProxy(){
            startScreenRecord();
        }
        public void stopScreenRecordProxy(){
            stopScreenRecord();
        }

        public boolean isRecordingProxy(){
            return isRecording;
        }

        public void onRecordPrepareProxy(int resultCode, Intent data) {
            onRecordPrepare(resultCode, data);
        }
    }

    private boolean isRecording = false;

    public void startScreenRecord(){
        session.startRecording();
        isRecording = true;
        // 发送广播通知ui开始录制ui更新
        Intent intent2ui = new Intent(VideoListFragment.ACTION_RECEIVER);
        intent2ui.putExtra(VideoListFragment.ARG_EVENTID, VideoListFragment.EVENTID_START_RECORD);
        sendBroadcast(intent2ui);
        // 把录制状态写入配置文件
        setBooleanPreference(Contants.PREFERENCE_KEY_ISRECORDING, true);
        // 在通知栏显示录制状态
        showNotification();

        IntentFilter filter = new IntentFilter(ACTION_RECEIVER);
        registerReceiver(new NotificationReceiver(), filter);
    }
    public void stopScreenRecord(){
        session.stopRecording();
        isRecording = false;
        // 发送广播通知ui开始录制ui更新
        Intent intent2ui = new Intent(VideoListFragment.ACTION_RECEIVER);
        intent2ui.putExtra(VideoListFragment.ARG_EVENTID, VideoListFragment.EVENTID_STOP_RECORD);
        sendBroadcast(intent2ui);
        // 把录制状态写入配置文件
        setBooleanPreference(Contants.PREFERENCE_KEY_ISRECORDING, false);
    }

    public boolean isRecording(){
        return isRecording;
    }

    public void onRecordPrepare(int resultCode, Intent data) {
        //  启动一个录屏会话
        session = new RecordingSession(ScreeenRecordService.this, resultCode, data);
        startScreenRecord();
    }



    /**
     * 显示通知栏信息
     */
    private void showNotification() {
        // 指定点击notification时要打开的activity
        Intent intent2activity = new Intent(this, Main.class);
        intent2activity.putExtra(Main.EXTRA_FRAGMENT_ID, 0);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Main.class);
        stackBuilder.addNextIntent(intent2activity);
        PendingIntent pendingIntent2activity =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        // 定义停止录制的pendingintent
        Intent intent2stop = new Intent(this, Main.class);
        intent2stop.putExtra(Main.EXTRA_FRAGMENT_ID, 0);
        intent2activity.putExtra(Main.EXTRA_EVENT_ID, Main.EVENT_ID_STOP_RECORD);
        PendingIntent pendingIntent2stop = PendingIntent.getActivity(this, 0, intent2stop, PendingIntent.FLAG_CANCEL_CURRENT);

        // 定义绘制的pendingintent
        Intent intent2edit = new Intent(ACTION_RECEIVER);
        PendingIntent pendingIntent2edit = PendingIntent.getBroadcast(this, 5, intent2edit, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_app)
                .setContentTitle(getResources().getText(R.string.app_name))
                .setContentText(getResources().getText(R.string.notification_recording))
                .setContentIntent(pendingIntent2activity)
                .addAction(R.drawable.ic_stop_black_24dp, getResources().getText(R.string.notification_stop), pendingIntent2stop)
                .addAction(R.drawable.ic_mode_edit_black_24dp, getResources().getText(R.string.notification_edit), pendingIntent2edit)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID_RECORDING, notification);
    }

    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == ACTION_RECEIVER){
                stopScreenRecord();
            }
        }
    }
}
