package com.jalen.screenrecord.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.jalen.screenrecord.R;
import com.jalen.screenrecord.activity.Main;
import com.jalen.screenrecord.activity.VideoPlayer;
import com.jalen.screenrecord.adapter.VideoAdapter;
import com.jalen.screenrecord.bean.VideoBean;
import com.jalen.screenrecord.service.ScreeenRecordService;
import com.jalen.screenrecord.util.FileUtil;
import com.melnykov.fab.FloatingActionButton;
import com.tt.whorlviewlibrary.WhorlView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 视频列表显示界面
 */
public class VideoListFragment extends BaseFragment implements VideoAdapter.OnVideoItemClickListener {
    private static final int REQUEST_CREATE_SCREEN_CAPTURE = 0x0001;
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ACTION_RECEIVER = "android.intent.action.ui.record";
    public static final String ARG_EVENTID = "event_id";
    public static final int EVENTID_START_RECORD = 0x1001;
    public static final int EVENTID_STOP_RECORD = 0x1002;

    private RecyclerView mRecyclerView;

    private WhorlView mLoading;

    private View emptyView;

    private VideoAdapter mAdapter;

    private String mVideoFileDir;

    private UIReceiver mUIReceiver;

    private int mEventId = -1;

    private ScreenRecordConnection mConn;
    /**
     * 录制悬浮按钮
     */
    private FloatingActionButton btnRecord;

    private ScreeenRecordService.ScreenRecordController mController;

    /**
     * @param sectionNumber 在navigation中的position
     * @param eventId
     * @return
     */
    public static VideoListFragment newInstance(int sectionNumber, int eventId) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_EVENTID, eventId);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initRecycerView(view);

        emptyView = view.findViewById(R.id.empty);
        mLoading = (WhorlView) view.findViewById(R.id.whorl_loading);

        // RecordButton设置
        btnRecord = (FloatingActionButton) view.findViewById(R.id.btn_record);
        boolean isRecording = (mController != null && mController.isRecordingProxy());
        btnRecord.setImageResource(isRecording ? R.drawable.ic_stop_white_24dp : R.drawable.ic_action_record);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mController != null && mController.isRecordingProxy()) {
                    mController.stopScreenRecordProxy();
                } else {
                    // 获取屏幕录制授权
                    MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getActivity().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                    Intent intent2ScreenCapture = mediaProjectionManager.createScreenCaptureIntent();
                    startActivity(intent2ScreenCapture);
                    startActivityForResult(intent2ScreenCapture, REQUEST_CREATE_SCREEN_CAPTURE);
                }
            }
        });

        loadData();
    }

    /**
     * 初次打开该fragment时对数据进行加载
     */
    private void loadData() {
        // 启动扫描线程
        mLoading.start();
        mLoading.setVisibility(View.VISIBLE);
        new Thread(new ScanRunnable()).start();
    }

    /**
     * RecyclerView初始化
     *
     * @param view
     */
    private void initRecycerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(android.R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(getLayoutManager());

    }

    /**
     * 获取RecyclerView的LayoutManager
     *
     * @return
     */
    private LinearLayoutManager getLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return layoutManager;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
        mEventId = getArguments().getInt(ARG_EVENTID);

        // 注册广播接收者，监听来自service的UI更新请求
        mUIReceiver = new UIReceiver();
        IntentFilter filter = new IntentFilter(ACTION_RECEIVER);
        getActivity().registerReceiver(mUIReceiver, filter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mUIReceiver != null) {
            getActivity().unregisterReceiver(mUIReceiver);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CREATE_SCREEN_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("joyce", "获取屏幕录制授权成功");
                // 告诉代理人已经获取录制权限了，请准备录制工作
                mController.onRecordPrepareProxy(resultCode, data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 绑定服务
        if (mConn == null) {
            mConn = new ScreenRecordConnection();
            getActivity().bindService(new Intent(getActivity(), ScreeenRecordService.class), mConn, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mController != null && mEventId == EVENTID_STOP_RECORD) {
            mController.stopScreenRecordProxy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mConn != null) {
            getActivity().unbindService(mConn);
        }
    }

    @Override
    public void onImageClick(View view, VideoBean videoBean) {
        Intent intent2player = new Intent(getContext(), VideoPlayer.class);
        intent2player.putExtra(VideoPlayer.EXTRA_VIDEO_PATH, videoBean.getVideoPath());
        startActivity(intent2player);
    }

    @Override
    public void onDeleteClick(View view, VideoBean videoBean) {

    }

    @Override
    public void onShareClick(View view, VideoBean videoBean) {

    }

    /**
     * 视频文件扫描子线程
     */
    private class ScanRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                List<VideoBean> mData = FileUtil.scanVideoFile(mVideoFileDir);
                if (mAdapter != null) {
                    mAdapter.setmData(mData);
                } else {
                    mAdapter = new VideoAdapter( mData);
                }
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
                switch (msg.what) {
                    case SCAN_STATE_COMPLETED:
                        fragment.mLoading.stop();
                        fragment.mLoading.setVisibility(View.GONE);

                        if (fragment.mAdapter.getData() == null || fragment.mAdapter.getData().size() == 0){
                            fragment.showEmptyView();
                        }else {
                            fragment.mRecyclerView.setVisibility(View.VISIBLE);
                            fragment.btnRecord.setVisibility(View.VISIBLE);
                            fragment.emptyView.setVisibility(View.GONE);

                            fragment.mRecyclerView.setAdapter(fragment.mAdapter);
                            fragment.mAdapter.notifyDataSetChanged();
                        }

                        break;
                }
            }
        }
    }

    private void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        btnRecord.setVisibility(View.VISIBLE);
    }

    private class ScreenRecordConnection implements ServiceConnection {
        private static final String tag = "ScreenRecordConnection";

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(tag, "服务链接成功");
            mController = (ScreeenRecordService.ScreenRecordController) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(tag, "服务断开链接成功");
        }
    }

    /**
     * 接收service发过来的信息
     */
    private class UIReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent data) {
            int eventId = data.getIntExtra(VideoListFragment.ARG_EVENTID, 0);
            switch (eventId) {
                case EVENTID_START_RECORD:
                    btnRecord.setImageResource(R.drawable.ic_stop_white_24dp);
                    break;
                case EVENTID_STOP_RECORD:
                    btnRecord.setImageResource(R.drawable.ic_action_record);
                    break;
            }
        }
    }
}
