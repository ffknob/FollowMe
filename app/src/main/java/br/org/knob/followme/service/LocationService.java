package br.org.knob.followme.service;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Date;
import java.util.List;

import br.org.knob.android.framework.service.GenericService;
import br.org.knob.android.framework.util.Util;
import br.org.knob.followme.dao.LocationDAO;
import br.org.knob.followme.model.Location;

public class LocationService extends GenericService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "LocationService";

    private Context context;
    private List<Location> locations;
    private LocationDAO locationDAO;

    private GoogleApiClient mGoogleApiClient;

    public LocationService(Context context) {
        this.context = context;
        locationDAO = new LocationDAO(context);

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void save(Location location) {
        locationDAO.save(location);
    }

    public long count() {
        return locationDAO.count();
    }

    public Location getLastKnowLocation() {
        Location location = null;

        mGoogleApiClient.connect();

        if(mGoogleApiClient.isConnected()) {
            android.location.Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if(lastKnownLocation != null) {
                Util.log(TAG, "Last know location: " + lastKnownLocation.toString());
                location = new Location(
                        new Date(),
                        String.valueOf(lastKnownLocation.getLatitude()),
                        String.valueOf(lastKnownLocation.getLongitude()));
            }

            mGoogleApiClient.disconnect();
        }

        return location;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Util.log(TAG, "Connected to Google Play Services");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Util.log(TAG, "Connection suspended: " + cause);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Util.log(TAG, "Connection failed: " + connectionResult.toString());
    }
}
