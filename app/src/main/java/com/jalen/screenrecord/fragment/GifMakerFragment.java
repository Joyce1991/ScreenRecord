package com.jalen.screenrecord.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.jalen.screenrecord.R;
import com.jalen.screenrecord.activity.Main;
import com.jalen.screenrecord.encoder.AnimatedGifEncoder;
import com.jalen.screenrecord.service.ScreeenRecordService;
import com.jalen.screenrecord.widget.RangeSeekBar;
import com.melnykov.fab.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Gif图片生产
 */
public class GifMakerFragment extends BaseFragment {
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int REQUEST_VIDEO_SELECT = 0x001;

    private FloatingActionButton fabAdd;
    private Spinner fps;
    private RangeSeekBar seekBar;
    private VideoView videoView;
    private TextView tvMinTime;
    private TextView tvMaxTime;

    private MediaMetadataRetriever mmRetriever;
    private File outFile;
    private long maxDur;


    public static GifMakerFragment newInstance(int sectionNumber, String param2) {
        GifMakerFragment fragment = new GifMakerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GifMakerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mmRetriever = new MediaMetadataRetriever();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gif_maker, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabAdd = (FloatingActionButton) view.findViewById(R.id.btn_add);
        videoView = (VideoView) view.findViewById(R.id.videoView);
        seekBar = (RangeSeekBar) view.findViewById(R.id.sb_start_end);
        tvMaxTime = (TextView) view.findViewById(R.id.tv_end_time);
        tvMinTime = (TextView) view.findViewById(R.id.tv_start_time);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开视频文件选择
                Intent intent4VideoSelect = new Intent();
                intent4VideoSelect.setType("video/*");
                intent4VideoSelect.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent4VideoSelect, REQUEST_VIDEO_SELECT);
            }
        });
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                Log.d(tag, minValue + " : " + maxValue);
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 设置seekbar的区间值和初始值
                seekBar.setRangeValues(0, mp.getDuration());
                tvMinTime.setText(getTimeForTrackFormat(0, true));
                tvMaxTime.setText(getTimeForTrackFormat(mp.getDuration(), true));
                // 设置seekbar变化监听
                seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Integer minValue, Integer maxValue) {
                        // 更新seekbar的值
                        tvMinTime.setText(getTimeForTrackFormat(minValue, true));
                        tvMaxTime.setText(getTimeForTrackFormat(maxValue, true));
                        // 更新视频播放位置
                        videoView.seekTo(minValue);
                    }
                });
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_VIDEO_SELECT:
                if (resultCode == Activity.RESULT_OK) {
                    //   判断data是否为空，data中的是否有数据
                    if (data != null && data.getData() != null) {
                        Uri uri = data.getData();

                        videoView.setVideoURI(uri);
                        videoView.start();
                    }
                    break;
                }
        }
    }

    public static String getTimeForTrackFormat(int timeInMills, boolean display2DigitsInMinsSection) {
        int minutes = (timeInMills / (60 * 1000));
        int seconds = (timeInMills - minutes * 60 * 1000) / 1000;
        String result = display2DigitsInMinsSection && minutes < 10 ? "0" : "";
        result += minutes + ":";
        if (seconds < 10) {
            result += "0" + seconds;
        } else {
            result += seconds;
        }
        return result;
    }

    class ConvertTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {
            generate(720, 480, outFile.getPath());
            return null;
        }

        private void generate(int requestWidth, int requestHeight,
                              String outputPath) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // gif编码器
            AnimatedGifEncoder animatedGifEncoder = new AnimatedGifEncoder();
            animatedGifEncoder.setDelay(100);
            animatedGifEncoder.start(bos);
            animatedGifEncoder.setRepeat(0); // 无止境循环

            // 这里取一百帧连成gif动画
            Bitmap bmFrame;
            long startMillSeconds = 0;
            long periodMillSeconds = 1000;
            for (long time = startMillSeconds; time < maxDur; time += periodMillSeconds) {
                bmFrame = mmRetriever.getFrameAtTime(time * 1000,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                bmFrame = ThumbnailUtils.extractThumbnail(bmFrame,
                        requestWidth, requestHeight,
                        ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                if (bmFrame == null) {
                    continue;
                }
                // bmFrame = compressImage(bmFrame);
                try {
                    animatedGifEncoder.addFrame(bmFrame);
                   /* if (mGifListener != null) {
                        // mGifListener.onMake(time, maxDur);
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                    System.gc();
                    break;
                }
            }

            animatedGifEncoder.finish();
            mmRetriever.release();

            try {
                byte[] data = bos.toByteArray();
                File file = new File(outputPath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

