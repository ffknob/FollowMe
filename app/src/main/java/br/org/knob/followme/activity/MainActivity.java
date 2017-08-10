package br.org.knob.followme.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.org.knob.android.framework.activity.BaseActivity;
import br.org.knob.android.framework.manager.KafManager;
import br.org.knob.android.framework.model.Location;
import br.org.knob.android.framework.service.LocationService;
import br.org.knob.android.framework.service.MapService;
import br.org.knob.android.framework.service.SettingsService;
import br.org.knob.android.framework.util.Util;
import br.org.knob.followme.R;
import br.org.knob.followme.adapter.HistoryAdapter;
import br.org.knob.followme.adapter.SettingsAdapter;
import br.org.knob.followme.fragment.HistoryFragment;
import br.org.knob.followme.fragment.MapFragment;
import br.org.knob.followme.service.FollowMeIntentService;
import br.org.knob.followme.settings.Settings;

public class MainActivity
        extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    HistoryFragment.OnItemSelectedListener,
                    HistoryFragment.OnItemSwipedListener,
                    HistoryFragment.OnItemLongClickedListener {
    private static final String TAG = "MainActivity";

    private LocationService locationService;
    private MapService mapService;

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

        // TODO: remove
        // Clearing all preferences to test the default values
        //PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();

        // Check if app settings have already been initialized
        if (!settingsService.isInitialized()) {
            // If not, do it now
            settings.initialize();
            settingsService.commitToSharedPreferences();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.bringToFront();
                invalidateOptionsMenu();
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Intent service
        Intent startServiceIntent = new Intent(this, FollowMeIntentService.class);
        startService(startServiceIntent);

        // App services
        locationService = new LocationService(this);
        mapService = new MapService(this);
        mapService.connect();

        // First fragment (map)
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            // Get last know location
            Location lastKnownLocation = mapService.getLastKnowLocation();

            // Map fragment
            MapFragment mapFragment = new MapFragment();
            Bundle fragmentBundle = new Bundle();
            fragmentBundle.putSerializable(MapFragment.LOCATION_KEY, lastKnownLocation);
            mapFragment.setArguments(fragmentBundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mapFragment).commit();
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
    public void onResume() {
        super.onResume();
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

        toggleHideyBar();

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
        int id = item.getItemId();

        if (id == R.id.nav_drawer_map) {
            // Get last know location
            Location lastKnownLocation = mapService.getLastKnowLocation();

            // Map fragment
            MapFragment mapFragment = new MapFragment();
            Bundle fragmentBundle = new Bundle();
            fragmentBundle.putSerializable("location", lastKnownLocation);
            fragmentBundle.putBoolean("animate-camera", false);
            mapFragment.setArguments(fragmentBundle);

            // Map fragment
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, mapFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_drawer_history) {
            // History fragment
            HistoryFragment historyFragment = new HistoryFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, historyFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_drawer_share) {

        } else if (id == R.id.nav_drawer_about) {

        } else if (id == R.id.nav_drawer_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus) {
            toggleHideyBar();
        }
    }

    // https://developer.android.com/samples/ImmersiveMode/src/com.example.android.immersivemode/ImmersiveModeFragment.html#l75
    public void toggleHideyBar() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    @Override
    public void onHistoryLocationSelected(Location location) {
        Util.toast(this,"Selected location #" + location.getId());

        // Map fragment
        MapFragment mapFragment = new MapFragment();
        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putSerializable(MapFragment.LOCATION_KEY, location);
        mapFragment.setArguments(fragmentBundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mapFragment).commit();
    }

    @Override
    public void onHistoryLocationSwiped(Location location, HistoryAdapter historyAdapter, int idx) {
        Util.toast(this,"Swiped location #" + location.getId());
        historyAdapter.getItens().remove(idx);
        historyAdapter.notifyItemRemoved(idx);
    }

    @Override
    public void onHistoryLocationLongClicked(Location location, HistoryAdapter historyAdapter, int idx, View view) {
        Util.toast(this,"Long clicked location #" + location.getId());
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
    }
}
