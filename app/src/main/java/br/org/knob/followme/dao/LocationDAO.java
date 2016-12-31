package br.org.knob.followme.dao;

import android.content.Context;

import br.org.knob.android.framework.dao.GenericDAO;
import br.org.knob.android.framework.database.DatabaseHelper;
import br.org.knob.followme.model.Location;
import br.org.knob.followme.settings.Settings;


public class LocationDAO extends GenericDAO<Location> {
    public static final String TAG = "LocationDAO";

    private static final String TABLE_NAME = "locations";
    private static final String ID_COLUMN_NAME = "_id";

    public LocationDAO(Context context) {
        super(Location.class);
        // TODO: get from settings
        dbHelper = new DatabaseHelper(context,  Settings.DEFAULT_DATABASE_NAME, Settings.DEFAULT_DATABASE_VERSION);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getIdColumnName() { return ID_COLUMN_NAME; }
}
