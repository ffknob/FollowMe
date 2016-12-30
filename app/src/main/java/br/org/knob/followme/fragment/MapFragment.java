package br.org.knob.followme.fragment;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import br.org.knob.android.framework.fragment.BaseFragment;
import br.org.knob.android.framework.util.Util;
import br.org.knob.followme.R;
import br.org.knob.followme.service.LocationService;

public class MapFragment extends BaseFragment
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MapFragment";

    private SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap map;

    private Location lastKnownLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get last known location FAB button
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_get_last_know_location);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Last know location
                lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                Util.log(TAG, "Last know location: " + lastKnownLocation.toString());

                setMapLocation(lastKnownLocation);

                // Save to database
                br.org.knob.followme.model.Location location = new br.org.knob.followme.model.Location(
                        new Date(),
                        String.valueOf(lastKnownLocation.getLatitude()),
                        String.valueOf(lastKnownLocation.getLongitude()));

                LocationService locationService = new LocationService(getContext());
                locationService.save(location);

                String info = "Locations: " + locationService.count(); //Last know location: (" + location.getLatitude() + ", " + location.getLongitude() + ")";
                Snackbar.make(view, info, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        // Google API client
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Map fragment
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_map, mapFragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Util.log(TAG, "Map ready: " + googleMap);
        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void setMapLocation(Location location) {
        if (map != null && location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            map.animateCamera(update);

            Util.log(TAG, "Set map location: " + location.toString());

            CircleOptions circle = new CircleOptions().center(latLng);
            circle.fillColor(Color.MAGENTA);
            circle.radius(25);
            map.clear();
            map.addCircle(circle);
        }
    }
}
