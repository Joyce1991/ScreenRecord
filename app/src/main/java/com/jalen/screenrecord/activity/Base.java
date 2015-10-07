package com.jalen.screenrecord.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.jalen.screenrecord.Contants;
import com.jalen.screenrecord.R;


/**
 * <div>Activity基础类 <div/>
 * <div>Created by jalen on 2015/8/22.</div>
 */
public class Base extends AppCompatActivity {
    /**
     * 用于log打印，记录当前类名
     */
    public String tag;

    /**
     * 初始<code>Toolbar</code>和<code>Drawer</code>切换
     */
    private Toolbar mToolbar;
    /**
     * 配置设置辅助对象
     */
    private SharedPreferences sp;

    public Base(){
        super();
        tag = this.getClass().getSimpleName();

    }

    public boolean getBooleanPreferenceByKey(String key){
        sp = getSharedPreferences(Contants.APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
    public void setBooleanPreference(String key, boolean value){
        sp = getSharedPreferences(Contants.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,value);
        editor.commit();
        editor = null;
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

    // *************************************** ActionBar 设置 **************************************

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    protected Toolbar getActionBarToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

}
