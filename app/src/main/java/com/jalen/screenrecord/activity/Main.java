package com.jalen.screenrecord.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjectionManager;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;

import com.jalen.screenrecord.fragment.NavigationDrawerFragment;
import com.jalen.screenrecord.R;
import com.jalen.screenrecord.fragment.VideoListFragment;
import com.jalen.screenrecord.service.ScreeenRecordService;
import com.melnykov.fab.FloatingActionButton;

/**
 * 主activity
 * <div>参考文献：<a href="http://enginebai.logdown.com/posts/280450/android-toolbar-navigation-drawer">Toolbar + 套用Navigation Drawer</a></div>
 */
public class Main extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, View.OnClickListener {

    private static final int REQUEST_CREATE_SCREEN_CAPTURE = 0x0001;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    private ScreeenRecordService.ScreenRecordController mController;

    /**
     * 录制悬浮按钮
     */
    private FloatingActionButton btnRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 把statusbar设为透明，让colorprimarydark也可以在4.4显示出来，让toolbar延伸到statusbar
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/

        setContentView(R.layout.activity_main_under);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                getActionBarToolbar(),
                (DrawerLayout) findViewById(R.id.drawer_layout));

        btnRecord = (FloatingActionButton) this.findViewById(R.id.btn_record);
//        btnRecord.setImageResource(getBooleanPreferenceByKey(Contants.PREFERENCE_KEY_ISRECORDING) ? R.drawable.ic_stop_white_24dp : R.drawable.ic_action_record);
        btnRecord.setImageResource(R.drawable.ic_action_record);
        btnRecord.setOnClickListener(this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position == 0){
            Intent intent2Settings = new Intent(this, Settings.class);
            startActivity(intent2Settings);
        }else{
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, VideoListFragment.newInstance("params1","params2"))
                    .commit();
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_record:
                if(mController != null && mController.isRecording()){
                    mController.stopScreenRecord();
                    btnRecord.setImageResource(R.drawable.ic_action_record);
                }else{
                    MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) this.getSystemService(MEDIA_PROJECTION_SERVICE);
                    Intent intent2ScreenCapture = mediaProjectionManager.createScreenCaptureIntent();
                    startActivity(intent2ScreenCapture);
                    startActivityForResult(intent2ScreenCapture, REQUEST_CREATE_SCREEN_CAPTURE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CREATE_SCREEN_CAPTURE){
            if (resultCode == Activity.RESULT_OK){
                Log.d("joyce", "创建屏幕捕获成功");
                // 通过start方式开启service
                // 通过bind方式绑定屏幕录制服务
//                Intent intent2RecordService = new Intent(this. ScreenRecordService.class);
                bindService(ScreeenRecordService.newIntent(this, resultCode, data), new ScreenRecordConnection(), BIND_AUTO_CREATE);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class ScreenRecordConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mController = (ScreeenRecordService.ScreenRecordController) service;
            mController.startScreenRecord();
            btnRecord.setImageResource(R.drawable.ic_stop_white_24dp);
            Log.d("joyce", "获取控制器成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
