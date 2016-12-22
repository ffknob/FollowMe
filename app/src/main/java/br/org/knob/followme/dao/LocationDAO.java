package br.org.knob.followme.dao;

import android.content.Context;

import br.org.knob.android.framework.dao.GenericDAO;
import br.org.knob.android.framework.database.DatabaseHelper;
import br.org.knob.followme.model.Location;
import br.org.knob.followme.settings.Settings;


public class LocationDAO extends GenericDAO<Location> {
    public static final String TAG = "LocationDAO";

    private static final String TABLE_NAME = "locations";

    public LocationDAO(Context context) {
        super(Location.class);
        dbHelper = new DatabaseHelper(context,  Settings.DATABASE_NAME, Settings.DATABASE_VERSION);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
