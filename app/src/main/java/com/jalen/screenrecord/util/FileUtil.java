package com.jalen.screenrecord.util;

import com.jalen.screenrecord.AfferentException;
import com.jalen.screenrecord.bean.VideoBean;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理工具类
 * Created by joyce on 2015/10/15.
 */
public class FileUtil {
    /**
     * 扫描指定目录下是视频文件
     *
     * @param dirPath 目录路径
     * @return 返回文件列表
     */
    public static List<VideoBean> scanVideoFile(String dirPath) throws Throwable {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return null;
        }

        if (!dir.isDirectory()) {
            throw new AfferentException("你指定的文件路径不是一个目录");
        }

        // 创建一个文件过滤器
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return MediaFile.isVideoFile(pathname.getAbsolutePath());
            }
        };
        // 列出所有的视频文件
        File[] videoFiles = dir.listFiles(filter);

        // 遍历解析视频文件
        List<VideoBean> videoBeans = new ArrayList<>();
        if (videoFiles != null){
            for (File file : videoFiles) {
                VideoBean videoBean = new VideoBean();
                videoBean.setLength(file.length());
                videoBean.setLastModified(file.lastModified());
                videoBean.setVideoPath(file.getAbsolutePath());
                videoBean.setVideoName(file.getName());
                videoBeans.add(videoBean);
            }
        }
        return videoBeans;
    }
}
