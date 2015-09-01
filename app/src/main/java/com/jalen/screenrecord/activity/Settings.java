package com.jalen.screenrecord.activity;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.content.IntentCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jalen.screenrecord.R;
import com.jalen.screenrecord.fragment.SettingsFragment;
import com.jalen.screenrecord.fragment.SettingsFragment.OnFragmentInteractionListener;


/**
 * 全局设置
 * <div>参考资料：<a href="https://github.com/google/iosched/blob/master/android/src/main/java/com/google/samples/apps/iosched/ui/SettingsActivity.java">google i/o大会android客户端</a></div>
 */
public class Settings extends BaseActivity implements OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 对toolbar进行设置
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateUpToFromChild(Settings.this, IntentCompat.makeMainActivity(new ComponentName(Settings.this, Main.class)));
            }
        });

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SettingsFragment())
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
