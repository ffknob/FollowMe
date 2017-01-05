package br.org.knob.followme.service;

import android.app.IntentService;
import android.content.Intent;

import java.util.Timer;
import java.util.TimerTask;

import br.org.knob.android.framework.model.Location;
import br.org.knob.android.framework.service.LocationService;
import br.org.knob.android.framework.service.MapService;
import br.org.knob.followme.settings.Settings;

public class FollowMeIntentService extends IntentService {
    public static final String TAG = "FollowMeIntentService";

    public FollowMeIntentService(){
        super("FollowMe");
    }

    public FollowMeIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final LocationService locationService = new LocationService(this);
        final MapService mapService = new MapService(this);

        Integer getLocationIntervalSetting = (Integer)Settings.getInstance().get(Settings.FOLLOWME_SETTINGS_SERVICE_GET_LOCATION_INTERVAL);

        if(getLocationIntervalSetting != null) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Location location = mapService.getLastKnowLocation();
                    if (location != null) {
                        locationService.save(location);
                    }
                }
            }, getLocationIntervalSetting.intValue() * 60000);
        }
    }
}
