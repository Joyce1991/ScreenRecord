package com.jalen.screenrecord.activity;

import android.content.ComponentName;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.jalen.screenrecord.R;
import com.jalen.screenrecord.fragment.GifMakerFragment;

/**
 * gif制作页面
 * Created by joyce on 2015/10/9.
 */
public class GifMaker extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifmaker);

        // 对toolbar进行设置
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateUpToFromChild(GifMaker.this, IntentCompat.makeMainActivity(new ComponentName(GifMaker.this, Main.class)));
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new GifMakerFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gifmaker, menu);
        return true;
    }
}
