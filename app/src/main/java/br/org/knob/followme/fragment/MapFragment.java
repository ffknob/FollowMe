package br.org.knob.followme.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import br.org.knob.followme.model.Location;
import br.org.knob.followme.service.MapService;

public class MapFragment extends BaseFragment implements OnMapReadyCallback {
    private static final String TAG = "MapFragment";

    private SupportMapFragment mapFragment;
    private MapService mapService;
    private LocationService locationService;
    private Location lastKnownLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        locationService = new LocationService(getContext());
        mapService = new MapService(getContext());
        mapService.connect();

        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get last known location FAB button
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_get_last_know_location);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Last know location
                lastKnownLocation = mapService.getLastKnowLocation();

                // Save location
                locationService.save(lastKnownLocation);

                // Send map to that location
                mapService.setMapLocation(lastKnownLocation, true);

                String info = "Locations: " + locationService.count(); //Last know location: (" + location.getLatitude() + ", " + location.getLongitude() + ")";
                Snackbar.make(view, info, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
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

        if(!mapService.isMapReady()) {
            mapFragment.getMapAsync(this);
        } else {
            // Try to go to the location passed to the fragment
            Location location = null;
            Boolean animateCamera = false;
            Bundle bundle = getArguments();
            if(bundle != null) {
                location = (Location) bundle.getSerializable("location");
                animateCamera = (Boolean) bundle.getBoolean("animate-camera");
            }

            if(location != null) {
                // I have a location, let's go there
                mapService.setMapLocation(location, animateCamera);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        mapService.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Util.log(TAG, "Map ready: " + googleMap);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapService.setMap(googleMap);
    }
}
