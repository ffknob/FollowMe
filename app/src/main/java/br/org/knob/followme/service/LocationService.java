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

import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.org.knob.android.framework.service.GenericService;
import br.org.knob.android.framework.util.Util;
import br.org.knob.followme.dao.LocationDAO;
import br.org.knob.followme.model.Location;

public class LocationService extends GenericService {
    public static final String TAG = "LocationService";

    private Context context;
    private List<Location> locations;
    private LocationDAO locationDAO;

    public LocationService(Context context) {
        this.context = context;
        locationDAO = new LocationDAO(context);
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

    public List<Location> findAllOrderedByDate() {
        // TODO: Limit somehow... we don't want to retrieve all saved locations
        locations = locationDAO.findAll();

        if(locations != null) {
            Collections.sort(locations, new Location.DateDescComparator());
        }

        return locations;
    }

    public Location findLatest() {
        return locationDAO.findLast(locationDAO.getIdColumnName());
    }
}
