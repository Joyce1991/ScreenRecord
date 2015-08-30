package com.jalen.screenrecord.service;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.jalen.screenrecord.bean.RecordingBean;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xxx on 2015/8/26.
 */
final class RecordingSession {
    private static final String tag = "RecordingSession";
    private static final String DISPLAY_NAME = "screen_record";
    private Context mContext;
    private int mResultCode;
    private Intent mData;
    private MediaRecorder mMediaRecorder;
    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    /**
     * 录屏输出目录
     */
    private File mOutputRoot;
    /**
     * 设置输出文件路径
     */
    private String mOutputFile;
    /**
     * 文件名称格式
     */
    private final DateFormat fileFormat =
            new SimpleDateFormat("''yyyy年MM月dd日：HH时mm分ss'.mp4'", Locale.CHINA);
    /**
     * 虚拟显示屏
     */
    private VirtualDisplay mVirtualDisplay;

    RecordingSession(Context context, int resultCode, Intent data){
        this.mContext = context;
        this.mResultCode = resultCode;
        this.mData = data;

        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        mOutputRoot = new File(picturesDir, "ScreenRecord");
        String outputName = fileFormat.format(new Date());
        mOutputFile = new File(mOutputRoot, outputName).getAbsolutePath();

        mMediaProjectionManager = (MediaProjectionManager) mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);

    }

    /**
     * 屏幕录制开始
     */
    public void startRecording() {
        Log.d(tag, "屏幕录制开始……");

        // 1. 创建视频文件存放目录
        if (!mOutputRoot.exists()){
            if (!mOutputRoot.mkdirs()){
                Log.e(tag,"目录创建失败");
            }
        }

        // 2. 获取录屏参数
        RecordingBean recordingBean = getRecordingInfo();

        // 3. 设置录屏参数
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);   // 设置视频源
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  // 设置输出格式为mp4
        mMediaRecorder.setVideoFrameRate(30);   // 设置视频帧率
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);    // 设置视频解码方式
        mMediaRecorder.setVideoSize(recordingBean.getWidth(), recordingBean.getHeight());// 设置视频的捕获尺寸
        mMediaRecorder.setVideoEncodingBitRate(8*1000*1000);    // 设置视频解码比特率
        mMediaRecorder.setOutputFile(mOutputFile);   // 设置输出文件

        // 4. 录制准备
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 5.用获取的的MediaProjection来创建虚拟Display
        mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mData);
        Surface surface = mMediaRecorder.getSurface();
        mVirtualDisplay = mMediaProjection.createVirtualDisplay(
                DISPLAY_NAME,
                recordingBean.getWidth(),
                recordingBean.getHeight(),
                recordingBean.getDensity(),
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION,
                surface,
                null,
                null);

        // 6. 录制开始
        mMediaRecorder.start();
        Log.d(tag, "屏幕录制开始了");
    }

    /**
     * 停止屏幕录制方法
     */
    public void stopRecording() {
        Log.d(tag, "停止屏幕录制");

        // 1.停止projecton，所有数据flush到mediarecorder中
        mMediaProjection.stop();

        // 2. 停止mediarecorder，数据flush到文件中去
        mMediaRecorder.stop();

        // 3. 资源释放
        mMediaRecorder.release();
        mVirtualDisplay.release();

    }

    /**
     * 获取屏幕录制信息
     * @return 屏幕录制信息对象
     */
    private RecordingBean getRecordingInfo() {
        // 1. 获取设备屏幕参数
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        int displayDensity = displayMetrics.densityDpi;
        Log.d(tag, "Display size: " + displayWidth + ", " + displayHeight + ", " + displayDensity);

        // 2. 获取设备支持的最佳camera profile
        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        int cameraWidth = camcorderProfile != null ? camcorderProfile.videoFrameWidth : -1;
        int cameraHeight = camcorderProfile != null ? camcorderProfile.videoFrameHeight : -1;
        Log.d(tag, "Camera size: " + cameraWidth + ", " + cameraHeight);

        // 3. 获取尺寸缩放百分比
        int sizePercentage = 50;
        Log.d(tag, "Size percentage: " + sizePercentage);

        // 4. 获取当前屏幕的横竖屏状态
        Configuration configuration = mContext.getResources().getConfiguration();
        boolean isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;
        Log.d(tag, "Display landscape: " + isLandscape);

        // 5. 根据以上三项参数计算出屏幕录制信息
        return calculateRecordingInfo(displayWidth, displayHeight, displayDensity, isLandscape,
                cameraWidth, cameraHeight, sizePercentage);

    }

    /**
     * 显示正在录制的通知操作栏
     */
    private void showRecordingNotification() {

    }

    /**
     * 计算录屏信息
     * @param displayWidth 设备显示器宽度
     * @param displayHeight 设备显示器高度
     * @param displayDensity 设备显示器密度
     * @param isLandscape   设备是否为横屏
     * @param cameraWidth   设备支持的最大一帧宽度像素
     * @param cameraHeight  设备支持的最大一帧高度像素
     * @param sizePercentage    缩放百分比
     */
    private RecordingBean calculateRecordingInfo(int displayWidth, int displayHeight, int displayDensity, boolean isLandscape, int cameraWidth, int cameraHeight, int sizePercentage) {
        // 1. 根据百分比重新计算缩放后的屏幕尺寸
        displayWidth = displayWidth * sizePercentage / 100;
        displayHeight = displayHeight * sizePercentage / 100;

        // 2. 如果没有相机
        if (cameraWidth == -1 && cameraHeight == -1) {
            // No cameras. Fall back to the display size.
            return new RecordingBean(displayWidth, displayHeight, displayDensity);
        }

        // 3. 根据横竖屏状态设定捕获帧矩形宽高
        int frameWidth = isLandscape ? cameraWidth : cameraHeight;
        int frameHeight = isLandscape ? cameraHeight : cameraWidth;
        if (frameWidth >= displayWidth && frameHeight >= displayHeight) {
            // 一帧能够容纳屏幕的实际像素，使用屏幕实际像素.
            return new RecordingBean(displayWidth, displayHeight, displayDensity);
        }

        // 4.根据一帧的的宽高进行等比缩放
        if (isLandscape) {
            frameWidth = displayWidth * frameHeight / displayHeight;
        } else {
            frameHeight = displayHeight * frameWidth / displayWidth;
        }

        return new RecordingBean(frameWidth, frameHeight, displayDensity);
    }
}
