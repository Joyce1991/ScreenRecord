package com.jalen.screenrecord.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jalen.screenrecord.AfferentException;
import com.jalen.screenrecord.R;
import com.jalen.screenrecord.activity.VideoPlayer;
import com.jalen.screenrecord.adapter.VideoAdapter;
import com.jalen.screenrecord.bean.VideoBean;
import com.jalen.screenrecord.util.MediaFile;

import java.io.File;
import java.io.FileFilter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频列表显示界面
 */
public class VideoListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private AbsListView mListView;

    private VideoAdapter mAdapter;

    private MediaScannerConnection.MediaScannerConnectionClient mClient;

    private String mVideoFileDir;

    public static VideoListFragment newInstance(String param1, String param2) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public VideoListFragment() {
        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        mVideoFileDir = new File(picturesDir, "ScreenRecord").getAbsolutePath();
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
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mAdapter = new VideoAdapter(getActivity(),null);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 启动扫描线程
        showDialog(getText(R.string.dialog_scan_video));
        new Thread(new ScanRunnable()).start();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    /*    try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * 扫描指定目录下是视频文件
     * @param dirPath 目录路径
     * @return 返回文件列表
     */
   private List<VideoBean> scanVideoFile(String dirPath) throws Throwable{
        File dir = new File(dirPath);
       if (!dir.exists()){
           return null;
       }

       if(!dir.isDirectory()) {
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
       for (File file : videoFiles) {
           VideoBean videoBean = new VideoBean();
           videoBean.setLength(file.length());
           videoBean.setLastModified(file.lastModified());
           videoBean.setVideoPath(file.getAbsolutePath());
           videoBean.setVideoName(file.getName());
           videoBeans.add(videoBean);
       }
       return videoBeans;
   }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VideoBean videoBean = (VideoBean) mAdapter.getItem(position);
        Intent intent2player = new Intent(getActivity(), VideoPlayer.class);
        intent2player.putExtra(VideoPlayer.EXTRA_VIDEO_PATH, mAdapter.getData().get(position).getVideoPath());
        startActivity(intent2player);
    }

    private class ScanRunnable implements Runnable {
        @Override
        public void run() {
            try {
                List<VideoBean> mData = scanVideoFile(mVideoFileDir);
                mAdapter.setmData(mData);
                // 通知UI线程进行UI刷新
                uiHandler.obtainMessage(SCAN_STATE_COMPLETED).sendToTarget();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * c处理来自子线程的的ui更新请求
     */
    private UIHandler uiHandler = new UIHandler(this);
    private static final int SCAN_STATE_COMPLETED = 0x1001;
    private static class UIHandler extends Handler {
        private final WeakReference<VideoListFragment> mFragment;
        UIHandler(VideoListFragment fragment) {
            mFragment = new WeakReference<VideoListFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            VideoListFragment fragment = mFragment.get();
            if (fragment != null) {
                switch (msg.what){
                    case SCAN_STATE_COMPLETED:
                        fragment.dismissDialog();
                        fragment.mAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }
}
