package br.org.knob.followme.settings;

import br.org.knob.android.framework.settings.AbstractSettings;
import br.org.knob.followme.R;

public class Settings extends AbstractSettings {
    private static final String TAG = "Settings";

    // FollowMe settings
    public static final String FOLLOWME_SETTINGS_DATABASE_NAME = "followme-settings-database-name";
    public static final String FOLLOWME_SETTINGS_DATABASE_VERSION = "followme-settings-database-version";
    public static final String FOLLOWME_SETTINGS_SERVICE_GET_LOCATION_INTERVAL = "followme-settings-get-location-interval";

    // Defaults
    public static final String DEFAULT_DATABASE_NAME = "followme";
    public static final int DEFAULT_DATABASE_VERSION = 1;
    public static final int DEFAULT_GET_LOCATION_INTERVAL = 1; // 1 minute

    private static Settings settings;

    private Settings() {
    }

    public static Settings getInstance() {
        if(settings == null) {
            settings = new Settings();
        }

        return settings;
    }

    @Override
    public void initialize() {
        // Set the settings XML resource
        settings.set(AbstractSettings.PARAM_SETTINGS_RESOURCE_ID, R.xml.settings);

        // Inform that the settings have been initialized
        settings.set(AbstractSettings.PARAM_SETTINGS_IS_INITIALIZED, true);

        // Set initial values for each setting
        settings.set(FOLLOWME_SETTINGS_SERVICE_GET_LOCATION_INTERVAL, DEFAULT_GET_LOCATION_INTERVAL);
    }

    @Override
    public int getSettingsResource() {
        return R.xml.settings;
    }

    @Override
    public void set(String s, Object o) {

    }
}
