package com.philipp.paris.weatherapp.activities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.components.DashBoardFragment;
import com.philipp.paris.weatherapp.components.LocationDialogFragment;
import com.philipp.paris.weatherapp.components.MeasurementsFragment;
import com.philipp.paris.weatherapp.domain.Settings;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init UI
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_dashboard));
    }

    private void refreshCurrentFragment() {
        getFragmentManager().beginTransaction().detach(currentFragment)
                .attach(currentFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // init switch-location item
        Settings settings = new Settings(getApplicationContext());
        MenuItem item = menu.findItem(R.id.action_switch_location);
        item.setIcon(settings.showHomeLocationData() ? R.drawable.ic_menu_home : R.drawable.ic_location);
        item.setTitle(settings.showHomeLocationData() ? R.string.menu_show_gps : R.string.menu_show_home);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_switch_location) {
            // update menu item
            boolean showHomeData = item.isChecked();
            item.setIcon(showHomeData ? R.drawable.ic_menu_home : R.drawable.ic_location);
            item.setTitle(showHomeData ? R.string.menu_show_gps : R.string.menu_show_home);
            item.setChecked(!item.isChecked());

            // update settings
            Settings settings = new Settings(getApplicationContext());
            settings.setShowHomeLocationData(showHomeData);
            settings.persist();

            refreshCurrentFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                fragment = DashBoardFragment.newInstance();
                break;
            case R.id.nav_measurements:
                fragment = MeasurementsFragment.newInstance();
                break;
            case R.id.nav_forecast:
                break;
            case R.id.nav_set_as_home:
                DialogFragment dialog = new LocationDialogFragment();
                dialog.show(getFragmentManager(), "location_dialog");
                break;
            case R.id.nav_settings:
                this.startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        if (fragment != null) {
            currentFragment = fragment;
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            item.setChecked(true);
        }

        drawer.closeDrawers();
        return true;
    }
}
