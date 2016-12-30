package br.org.knob.followme.adapter;


import android.content.SharedPreferences;

import br.org.knob.android.framework.adapter.AbstractSettingsAdapter;
import br.org.knob.followme.settings.Settings;

public class SettingsAdapter extends AbstractSettingsAdapter {
    protected static final String TAG = "SettingsAdapter";

    @Override
    public Settings getSettings(SharedPreferences sharedPreferences) {
        Settings settings = Settings.getInstance();

        if(sharedPreferences != null) {
            settings.set(Settings.FOLLOWME_SETTINGS_SERVICE_GET_LOCATION_INTERVAL, sharedPreferences.getInt(Settings.FOLLOWME_SETTINGS_SERVICE_GET_LOCATION_INTERVAL, Settings.DEFAULT_GET_LOCATION_INTERVAL));
        }

        return settings;
    }
}