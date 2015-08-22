package com.jalen.screenrecord.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.jalen.screenrecord.R;


/**
 * <div>Activity基础类 <div/>
 * <div>Created by jalen on 2015/8/22.</div>
 */
public class BaseActivity extends AppCompatActivity {
    public String tag;
    /**
     * 初始<code>Toolbar</code>和<code>Drawer</code>切换
     */
    private Toolbar mActionBarToolbar;

    public BaseActivity(){
        super();
        tag = this.getClass().getSimpleName();
    }

    // ******************************打印Activity生命周期*******************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(tag, "---------onCreat ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(tag, "---------onStart ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(tag, "---------onResume ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(tag, "---------onStop ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(tag, "---------onPause ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(tag, "---------onRestart ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // ***************************** 显示一条Toast *************************************************

    public void showToast(CharSequence msg, Exception e, boolean b) {
        Log.i(tag, msg.toString());
        if(b){
            Toast.makeText(
                    this,
                    msg,
                    Toast.LENGTH_SHORT
            ).show();
        }
        if (e != null){
            e.printStackTrace();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }
}
