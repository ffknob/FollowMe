package br.org.knob.followme.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.org.knob.android.framework.activity.BaseActivity;
import br.org.knob.android.framework.database.DatabaseHelper;
import br.org.knob.android.framework.manager.KafManager;
import br.org.knob.android.framework.service.SettingsService;
import br.org.knob.android.framework.util.Util;
import br.org.knob.followme.R;
import br.org.knob.followme.adapter.SettingsAdapter;
import br.org.knob.followme.service.FollowMeIntentService;
import br.org.knob.followme.settings.Settings;

public class MainActivity
        extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize framework
        KafManager kafManager = KafManager.getInstance(this);
        kafManager.initialize();

        // APP Settings
        Settings settings = Settings.getInstance(); // Get instance to initialize settings if user still doesn't have shared preferences for app
        SettingsService settingsService = new SettingsService(this, new SettingsAdapter());

        // Check if app settings have already been initialized
        if(!settingsService.isInitialized()) {
            // If not, do it now
            settings.initialize();
            settingsService.commitToSharedPreferences();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Database
        //this.deleteDatabase(Settings.DATABASE_NAME);
        // TODO: get from settings
        DatabaseHelper dbHelper = new DatabaseHelper(this, Settings.DEFAULT_DATABASE_NAME, Settings.DEFAULT_DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table if not exists locations (_id integer primary key autoincrement, date text not null, latitude text not null, longitude text not null);");

                Util.log(TAG, "Created table locations");
            }
        };

        dbHelper.getReadableDatabase();

        // Intent service
        Intent startServiceIntent =  new Intent(this, FollowMeIntentService.class);
        startService(startServiceIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.activity_main_action_find) {
            return true;
        } else if (id == R.id.activity_main_action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        }  else if (id == R.id.nav_drawer_share) {

        } else if (id == R.id.nav_drawer_about) {

        } else if (id == R.id.nav_drawer_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
