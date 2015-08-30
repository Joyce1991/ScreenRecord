package com.jalen.screenrecord.util;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import com.jalen.screenrecord.Contants;

/**
 * 图片工具类
 * Created by xxx on 2015/8/30.
 */
public class ImageUtil {
    public static Bitmap getVideoThumbnail(String path) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(path, Contants.THUMBNAIL_KIND);
        if (bitmap == null){
            throw new NullPointerException(path + ", 给视频文件创建缩略图失败,可能是因为你的你的视频文件是损坏了或者该视频文件不被当前系统支持");
        }
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, Contants.THUMBNAIL_WIDTH, Contants.THUMBNAIL_HEIGHT);
        return bitmap;
    }
}
