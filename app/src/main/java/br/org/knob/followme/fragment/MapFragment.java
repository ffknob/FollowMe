package br.org.knob.followme.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import br.org.knob.android.framework.fragment.BaseFragment;
import br.org.knob.android.framework.util.Util;
import br.org.knob.followme.R;
import br.org.knob.android.framework.service.LocationService;
import br.org.knob.android.framework.model.Location;
import br.org.knob.android.framework.service.MapService;

public class MapFragment extends BaseFragment implements OnMapReadyCallback {
    private static final String TAG = "MapFragment";

    private SupportMapFragment mapFragment;
    private MapService mapService;
    private LocationService locationService;

    private Location lastKnownLocation;
    private Boolean animateCamera;

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
                // Send map to that last known location when button clicked
                sendMapToLastKnownLocation(view);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            restoreState(getArguments());
        }

        // Map fragment
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_map, mapFragment).commit();
        }

        if(!mapService.isMapReady()) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Util.log(TAG, "Map ready: " + googleMap);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapService.setMap(googleMap);

        if(mapService.isMapReady()) {
            // Try to go to the location passed to the fragment
            if(lastKnownLocation != null) {
                // I have a location, let's go there
                mapService.setMapLocation(lastKnownLocation, animateCamera ? null : false);
            }
        }
    }

    private void saveState(Bundle outState) {
        outState.putSerializable("location", lastKnownLocation);
        outState.putSerializable("animate-camera", animateCamera);
    }

    private void restoreState(Bundle savedInstanceState) {
        lastKnownLocation = (Location) savedInstanceState.getSerializable("location");
        animateCamera = savedInstanceState.getBoolean("animate-camera");
    }

    private void sendMapToLastKnownLocation(View view) {
        // Send map to that last known location
        mapService.setLastKnownLocation();

        String info = "Locations: " + locationService.count(); //Last know location: (" + location.getLatitude() + ", " + location.getLongitude() + ")";
        Snackbar.make(view, info, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}
