package com.jalen.screenrecord.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.jalen.screenrecord.R;

/**
 * Created by xxx on 2015/8/31.
 */
public class VideoPlayer extends BaseActivity {
    public static final String EXTRA_VIDEO_PATH = "com.jalen.screenrecord.videopath";
    private static final String STATE_PLAY_POSITION = "position";

    private VideoView mVideoView;
    private MediaController mController;
    private String mVideoPath;
    private int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoPath = getIntent().getStringExtra(EXTRA_VIDEO_PATH);
        setContentView(R.layout.activity_videoplayer);

        mVideoView = (VideoView) this.findViewById(R.id.video_player);
        mVideoView.requestFocus();
        if (mController == null) {
            mController = new MediaController(this, true);
        }

        mVideoView.setMediaController(mController);
        mVideoView.setVideoPath(mVideoPath);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.seekTo(position);
                if (position == 0){
                    mVideoView.start();
                }else {
                    mVideoView.pause();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_PLAY_POSITION, mVideoView.getCurrentPosition());
        mVideoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt(STATE_PLAY_POSITION);
        mVideoView.seekTo(position);
    }
}
