package com.jalen.screenrecord;

import android.provider.MediaStore;

/**
 * Created by xxx on 2015/8/28.
 */
public interface Contants {
    String APP_PREFERENCES = "app_preferences";
    String PREFERENCE_KEY_ISRECORDING = "isrecording";

    int THUMBNAIL_WIDTH = 480;
    int THUMBNAIL_HEIGHT = 240;
    int THUMBNAIL_KIND = MediaStore.Images.Thumbnails.MINI_KIND;
}
