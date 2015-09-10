package com.jalen.screenrecord.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.jalen.screenrecord.R;

/**
 * 设置页面的内容fragment
 */
public class SettingsFragment extends PreferenceFragment {
    private OnFragmentInteractionListener mListener;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }


    private void setupSimplePreferencesScreen() {
        addPreferencesFromResource(R.xml.pref_general);

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
