package com.jalen.screenrecord.bean;

import android.graphics.Bitmap;

/**
 * 视频
 * Created by xxx on 2015/8/24.
 */
public class VideoBean {
    /**
     * 视频生成的时间
     */
    private long lastModified;
    /**
     * 视频文件地址
     */
    private String videoPath;
    /**
     * 视频文件大小（单位为：字节）
     */
    private long length;

    private String videoName;

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
}
