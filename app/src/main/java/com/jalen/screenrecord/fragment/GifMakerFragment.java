package com.jalen.screenrecord.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jalen.screenrecord.R;
import com.jalen.screenrecord.encoder.AnimatedGifEncoder;
import com.jalen.screenrecord.service.ScreeenRecordService;
import com.melnykov.fab.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Gif图片生产
 */
public class GifMakerFragment extends BaseFragment {
//    private OnGifListener mGifListener = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GifMakerFragment.
     */
    // TODO: Rename and change types and number of parameters
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


/*

    public boolean makeGif (List<Bitmap> source, String outputPath) throws IOException {
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        encoder.start(bos);
        encoder.setRepeat(0);
        final int length = source.size();
        for (int i = 0; i < length; i++) {
            Bitmap bmp = source.get(i);
            if (bmp == null) {
                continue;
            }
            Bitmap thumb = ThumbnailUtils.extractThumbnail(bmp, bmp.getWidth() / mRate, bmp.getHeight() / mRate, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            try {
                encoder.addFrame(thumb);
                if (mGifListener != null) {
                    mGifListener.onMake(i, length);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.gc();
                break;
            }
            //TODO how about releasing bitmap after addFrame
        }
        encoder.finish();
        source.clear();
        byte[] data = bos.toByteArray();
        File file = new File(outputPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(data);
        fileOutputStream.flush();
        fileOutputStream.close();
        return file.exists();
    }


}
*/
}
