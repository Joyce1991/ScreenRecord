package com.jalen.screenrecord.observer;

import android.os.FileObserver;

/**
 * 监听存放屏幕录制视频的目录变化
 * <div>参考资料：<a href="http://blog.csdn.net/andyhuabing/article/details/9044593">FileObserver 类用法及限制</a></div>
 * Created by xxx on 2015/8/30.
 */
public class VideoFileObserver extends FileObserver{
    static final int mask =(FileObserver.CREATE |
            FileObserver.DELETE|
            FileObserver.MODIFY);

    public VideoFileObserver(String path) {
        super(path, mask);
    }

    @Override
    public void onEvent(int event, String path) {
        switch (event) {
            case FileObserver.CREATE:

                break;
            case FileObserver.DELETE:

                break;
            case FileObserver.MODIFY:

                break;
        }
    }
}
