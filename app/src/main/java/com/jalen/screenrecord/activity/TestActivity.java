package com.jalen.screenrecord.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.jalen.screenrecord.R;

import java.io.File;

import static android.os.Environment.DIRECTORY_MOVIES;

public class TestActivity extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);



        new Thread(){
            @Override
            public void run() {
                File picturesDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES);
                File mOutputRoot = new File(picturesDir, "ScreenRecord");
                if (!mOutputRoot.exists()){
                    if (!mOutputRoot.mkdir()){
                        Log.e(tag, "目录创建失败");
                    }
                }
            }
        }.start();

    }
}
