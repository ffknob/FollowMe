package br.org.knob.followme.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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

import br.org.knob.android.framework.activity.BaseActivity;
import br.org.knob.android.framework.database.DatabaseHelper;
import br.org.knob.android.framework.util.Util;
import br.org.knob.followme.R;
import br.org.knob.followme.service.FollowMeIntentService;
import br.org.knob.followme.service.LocationService;
import br.org.knob.followme.settings.Settings;

public class MainActivity
        extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainActivity";

    protected GoogleMap map;
    private SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;

    private Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

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

                // TODO: remove fragment
                /*getSupportFragmentManager()
                        .beginTransaction()
                        .remove(mapFragment)
                        .commit();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Database
        //this.deleteDatabase(Settings.DATABASE_NAME);
        DatabaseHelper dbHelper = new DatabaseHelper(this, Settings.DATABASE_NAME, Settings.DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table if not exists locations (_id integer primary key autoincrement, date text not null, latitude text not null, longitude text not null);");

                Util.log(TAG, "Created table locations");
            }
        };

        dbHelper.getReadableDatabase();

        // Map
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.content_main_map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Intent service
        Intent startServiceIntent =  new Intent(this, FollowMeIntentService.class);
        startService(startServiceIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
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
        if(map != null && location != null) {
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
