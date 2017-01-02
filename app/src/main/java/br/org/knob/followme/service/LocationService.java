package br.org.knob.followme.service;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Collections;
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

    public long count() {
        return locationDAO.count();
    }

    public void save(Location location) {
        try {
            locationDAO.begin();
            locationDAO.save(location);
            locationDAO.commit();
        } catch(Exception e) {
            locationDAO.rollback();
        }
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

        if(location == null) {
            // Couldn't get last know location from GPS, will try to get it from saved locations
            location = locationDAO.findLast(locationDAO.getIdColumnName());
        }

        return location;
    }

    public List<Location> findAllOrderedByDate() {
        // TODO: Limit somehow... we don't want to retrieve all saved locations
        locations = locationDAO.findAll();

        if(locations != null) {
            Collections.sort(locations, new Location.DateDescComparator());
        }

        return locations;
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
