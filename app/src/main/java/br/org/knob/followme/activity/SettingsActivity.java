package br.org.knob.followme.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.org.knob.android.framework.activity.BaseActivity;
import br.org.knob.android.framework.fragment.SettingsFragment;
import br.org.knob.android.framework.settings.AbstractSettings;
import br.org.knob.followme.R;

public class SettingsActivity extends BaseActivity {
    protected static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getToolbar();
        if(actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Prepares the settings fragment - must pass the settings resource id
        SettingsFragment settingsFragment = new SettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AbstractSettings.PARAM_SETTINGS_RESOURCE_ID, R.xml.settings);
        settingsFragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_settings, settingsFragment)
                .commit();

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}

