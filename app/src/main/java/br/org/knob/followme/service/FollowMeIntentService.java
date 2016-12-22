package br.org.knob.followme.service;

import android.app.IntentService;
import android.content.Intent;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import br.org.knob.followme.R;
import br.org.knob.followme.model.Location;
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

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Location location = locationService.getLastKnowLocation();
                if(location != null) {
                    locationService.save(location);
                }
            }
        }, Settings.SERVICE_GET_LOCATION_INTERVAL);
    }
}
