package br.org.knob.followme.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.org.knob.followme.R;
import br.org.knob.android.framework.activity.BaseActivity;
import br.org.knob.android.framework.fragment.SettingsFragment;

public class FollowMeSettingsActivity extends BaseActivity {
    protected static final String TAG = "FollowMeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followme_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getToolbar();
        if(actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.activity_followme_settings_content, new SettingsFragment())
                .commit();

        PreferenceManager.setDefaultValues(this, R.xml.followme_settings, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.drawable.ic_action_back:
                finish();
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}

