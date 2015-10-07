package com.jalen.screenrecord.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.jalen.screenrecord.fragment.CutFragment;
import com.jalen.screenrecord.fragment.FeedbackFragment;
import com.jalen.screenrecord.fragment.GifMakerFragment;
import com.jalen.screenrecord.fragment.NavigationDrawerFragment;
import com.jalen.screenrecord.R;
import com.jalen.screenrecord.fragment.VideoListFragment;

/**
 * 主activity
 * <div>参考文献：<a href="http://enginebai.logdown.com/posts/280450/android-toolbar-navigation-drawer">Toolbar + 套用Navigation Drawer</a></div>
 */
public class Main extends Base
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    public static final String EXTRA_FRAGMENT_ID = "fragment_id";
    public static final String EXTRA_EVENT_ID = "event_id";
    public static final int EVENT_ID_STOP_RECORD = 0x1002;
    public static final int EVENT_ID_EDIT = 0x1003;
    public static final int EVENT_ID_NULL = -1;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

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

        Intent data = getIntent();
        if (data != null){
            int fragmentId = data.getIntExtra(Main.EXTRA_FRAGMENT_ID, -1);
            int eventId = data.getIntExtra(Main.EXTRA_EVENT_ID, -1);
            if (fragmentId != -1){
                onNavigationDrawerItemSelected(fragmentId, eventId);
            }
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position, int eventId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, VideoListFragment.newInstance(0, eventId))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, GifMakerFragment.newInstance(1, "params2"))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CutFragment.newInstance(2, "params2"))
                        .commit();
                break;
            case 3:
                Intent intent2Settings = new Intent(this, Settings.class);
                startActivity(intent2Settings);
                break;
            case 4:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, FeedbackFragment.newInstance(4, "params2"))
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        String[] items = getResources().getStringArray(R.array.drawer_items_text);
        mTitle = items[number];
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
}
