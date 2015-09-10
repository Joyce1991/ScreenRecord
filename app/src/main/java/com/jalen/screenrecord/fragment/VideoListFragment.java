package com.jalen.screenrecord.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jalen.screenrecord.AfferentException;
import com.jalen.screenrecord.R;
import com.jalen.screenrecord.activity.Main;
import com.jalen.screenrecord.activity.VideoPlayer;
import com.jalen.screenrecord.adapter.VideoAdapter;
import com.jalen.screenrecord.bean.VideoBean;
import com.jalen.screenrecord.service.ScreeenRecordService;
import com.jalen.screenrecord.util.MediaFile;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.io.FileFilter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频列表显示界面
 */
public class VideoListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private static final int REQUEST_CREATE_SCREEN_CAPTURE = 0x0001;
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SECTION_NUMBER = "section_number";

    private String mParam1;
    private String mParam2;

    private AbsListView mListView;

    private VideoAdapter mAdapter;

    private MediaScannerConnection.MediaScannerConnectionClient mClient;

    private String mVideoFileDir;

    /**
     * 录制悬浮按钮
     */
    private FloatingActionButton btnRecord;
    private ScreeenRecordService.ScreenRecordController mController;

    /**
     *
     * @param sectionNumber 在navigation中的position
     * @param param2
     * @return
     */
    public static VideoListFragment newInstance(int sectionNumber, String param2) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
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
        btnRecord = (FloatingActionButton) view.findViewById(R.id.btn_record);
//        btnRecord.setImageResource(getBooleanPreferenceByKey(Contants.PREFERENCE_KEY_ISRECORDING) ? R.drawable.ic_stop_white_24dp : R.drawable.ic_action_record);
        btnRecord.setImageResource(R.drawable.ic_action_record);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mController != null && mController.isRecording()) {
                    mController.stopScreenRecord();
                    btnRecord.setImageResource(R.drawable.ic_action_record);
                } else {
                    MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getActivity().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                    Intent intent2ScreenCapture = mediaProjectionManager.createScreenCaptureIntent();
                    startActivity(intent2ScreenCapture);
                    startActivityForResult(intent2ScreenCapture, REQUEST_CREATE_SCREEN_CAPTURE);
                }
            }
        });
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
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CREATE_SCREEN_CAPTURE){
            if (resultCode == Activity.RESULT_OK){
                Log.d("joyce", "创建屏幕捕获成功");
                // 通过start方式开启service
                // 通过bind方式绑定屏幕录制服务
//                Intent intent2RecordService = new Intent(this. ScreenRecordService.class);
//                getActivity().bindService(ScreeenRecordService.newIntent(getActivity(), resultCode, data), new ScreenRecordConnection(), Context.BIND_AUTO_CREATE);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().bindService(new Intent(getActivity(), ScreeenRecordService.class), new ScreenRecordConnection(), Context.BIND_AUTO_CREATE);
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

    private class ScreenRecordConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            /*mController = (ScreeenRecordService.ScreenRecordController) service;
            mController.startScreenRecord();
            btnRecord.setImageResource(R.drawable.ic_stop_white_24dp);*/
            Log.d("joyce", "获取控制器成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
