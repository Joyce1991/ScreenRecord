package com.jalen.screenrecord.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by jalen-pc on 2015/9/6.
 */
public class Env {
    /**
     * 获取apk的签名信息
     * @param context
     * @return
     */
    public static int getSign(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            return info.signatures[0].toCharsString().hashCode();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return -1;
    }
}
