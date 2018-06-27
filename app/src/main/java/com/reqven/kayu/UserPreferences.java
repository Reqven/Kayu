package com.reqven.kayu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;

public class UserPreferences extends PreferenceFragmentCompat {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String salt      = preferences.getString("salt", null);
        String sugar     = preferences.getString("sugar", null);
        String fat       = preferences.getString("fat", null);
        String saturated = preferences.getString("saturatedFat", null);

        //findPreference("salt").setSummary(salt);
        //findPreference("sugar").setSummary(sugar);
        //findPreference("fat").setSummary(fat);
        //findPreference("saturated_fat").setSummary(saturated);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }
}
