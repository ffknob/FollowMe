package br.org.knob.followme.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import br.org.knob.android.framework.service.GenericService;
import br.org.knob.android.framework.util.Util;
import br.org.knob.followme.model.Location;

public class MapService extends GenericService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MapFragment";

    private Context context;

    private static GoogleApiClient mGoogleApiClient;
    private GoogleMap map;

    public MapService(Context context) {
        this.context = context;

        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
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

    public boolean isMapReady() {
        return map != null;
    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
    }

    public boolean isConnected() {
        return mGoogleApiClient.isConnected();
    }

    public Location getLastKnowLocation() {
        Location location = null;

        if(mGoogleApiClient.isConnected()) {
            android.location.Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if(lastKnownLocation != null) {
                Util.log(TAG, "Last know location: " + lastKnownLocation.toString());

                // Try to get a snapshot from the location
                map.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {

                    }
                });

                location = new Location(
                        new Date(),
                        String.valueOf(lastKnownLocation.getLatitude()),
                        String.valueOf(lastKnownLocation.getLongitude()),
                        null);
            }
        }

        if(location == null) {
            // Couldn't get last know location from GPS, will try to get it from saved locations
            LocationService locationService = new LocationService(context);
            location = locationService.findLatest();
        }

        return location;
    }

    public void setMapLocation(Location location, boolean animateCamera) {
        if(location != null) {
            android.location.Location androidLocation = new android.location.Location(""); // Provider is not necessary
            androidLocation.setLatitude(new Double(location.getLatitude()));
            androidLocation.setLongitude(new Double(location.getLongitude()));

            if (map != null && androidLocation != null) {
                LatLng latLng = new LatLng(androidLocation.getLatitude(), androidLocation.getLongitude());

                if (animateCamera) {
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    map.animateCamera(update);
                }

                Util.log(TAG, "Set map location: " + androidLocation.toString());

                CircleOptions circle = new CircleOptions().center(latLng);
                circle.fillColor(Color.MAGENTA);
                circle.radius(25);
                map.clear();
                map.addCircle(circle);
            }
        }
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }
}
