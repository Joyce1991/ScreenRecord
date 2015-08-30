package com.jalen.screenrecord.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


/**
 * 视频录制服务
 */
public class ScreeenRecordService extends BaseService {
    private static final String EXTRA_RESULT_CODE = "result-code";
    private static final String EXTRA_DATA = "data";

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
        Log.d(tag, "服务绑定成功");

        // 1. 验证传递过来的信息是否为null
        int resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, Activity.RESULT_CANCELED);
        Intent data = intent.getParcelableExtra(EXTRA_DATA);
        if (resultCode == Activity.RESULT_CANCELED || data == null){
            throw new IllegalStateException("Result code or data missing.");
        }

        // 2. 启动一个录屏会话
        session = new RecordingSession(this, resultCode, data);

        // 3. 在这里把屏幕录制类控制器代理给前端，让他们持有控制器
        return new ScreenRecordController();
    }

    /**
     * 屏幕录制控制类
     */
    public class ScreenRecordController extends Binder {
        private boolean isRecording = false;

        public void startScreenRecord(){
            session.startRecording();
            isRecording = true;
        }
        public void stopScreenRecord(){
            session.stopRecording();
            isRecording = false;
        }

        public boolean isRecording(){
            return isRecording;
        }
    }
}
