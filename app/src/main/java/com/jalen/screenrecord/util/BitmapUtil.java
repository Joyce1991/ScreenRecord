package com.jalen.screenrecord.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;

import com.jalen.screenrecord.Contants;

/**
 * 图片工具类
 * Created by xxx on 2015/8/30.
 */
public class BitmapUtil {
    public static Bitmap getVideoThumbnail(String path) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(path, Contants.THUMBNAIL_KIND);
        if (bitmap == null){
            throw new NullPointerException(path + ", 给视频文件创建缩略图失败,可能是因为你的你的视频文件是损坏了或者该视频文件不被当前系统支持");
        }
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, Contants.THUMBNAIL_WIDTH, Contants.THUMBNAIL_HEIGHT);
        return bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
