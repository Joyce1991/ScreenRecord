package com.jalen.screenrecord.bean;

/**
 * 视频录制信息
 * Created by xxx on 2015/8/26.
 */
public class RecordingBean {
    /**
     * 宽度尺寸
     */
    private int width;
    /**
     * 高度尺寸
     */
    private int height;
    /**
     * 屏幕密度
     */
    private int density;

    public RecordingBean(int width, int height, int density) {
        this.width = width;
        this.height = height;
        this.density = density;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }
}
