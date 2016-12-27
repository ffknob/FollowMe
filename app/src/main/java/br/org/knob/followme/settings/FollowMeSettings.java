package br.org.knob.followme.settings;

import br.org.knob.android.framework.model.Setting;
import br.org.knob.android.framework.settings.AbstractSettings;
import br.org.knob.followme.R;

public class FollowMeSettings extends AbstractSettings {
    private static final String TAG = "FollowMeSettings";

    // FollowMe settings
    public static final String FOLLOWME_SETTINGS_DATABASE_NAME = "followme-settings-database-name";
    public static final String FOLLOWME_SETTINGS_DATABASE_VERSION = "followme-settings-database-version";
    public static final String FOLLOWME_SETTINGS_SERVICE_GET_LOCATION_INTERVAL = "followme-settings-get-location-interval";

    // Defaults
    public static final String DEFAULT_DATABASE_NAME = "followme";
    public static final int DEFAULT_DATABASE_VERSION = 1;
    public static final int DEFAULT_GET_LOCATION_INTERVAL = 60000; // 1 minute

    private static FollowMeSettings followMeSettings;

    private FollowMeSettings() {
        super();

        Setting getLocationInterval = new Setting<Integer>(FOLLOWME_SETTINGS_SERVICE_GET_LOCATION_INTERVAL, DEFAULT_GET_LOCATION_INTERVAL);
        settings.put(FOLLOWME_SETTINGS_SERVICE_GET_LOCATION_INTERVAL, getLocationInterval);

    }

    public static FollowMeSettings getInstance() {
        if(followMeSettings == null) {
            followMeSettings = new FollowMeSettings();
        }

        return followMeSettings;
    }

    @Override
    public int getSettingsResource() {
        return R.xml.followme_settings;
    }

    @Override
    public void set(String s, Object o) {

    }
}
