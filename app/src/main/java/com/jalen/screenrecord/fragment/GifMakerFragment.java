package com.jalen.screenrecord.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.jalen.screenrecord.R;
import com.jalen.screenrecord.encoder.AnimatedGifEncoder;
import com.jalen.screenrecord.service.ScreeenRecordService;
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
//    private OnGifListener mGifListener = null;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Button btnConvert;
    private Spinner fps;
    private MediaMetadataRetriever mmRetriever;
    private File outFile;
    private long maxDur;
//    private OnGifListener mGifListener = null;


    public static GifMakerFragment newInstance(String param1, String param2) {
        GifMakerFragment fragment = new GifMakerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gif_maker, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
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

