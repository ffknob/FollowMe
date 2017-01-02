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

    private Context context;

    public LocationDAO(Context context) {
        super(Location.class, context, Settings.DEFAULT_DATABASE_NAME, Settings.DEFAULT_DATABASE_VERSION);

        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public String getDbName() {
        return Settings.DEFAULT_DATABASE_NAME;
    }

    @Override
    public int getDbVersion() {
        return Settings.DEFAULT_DATABASE_VERSION;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getIdColumnName() { return ID_COLUMN_NAME; }
}
