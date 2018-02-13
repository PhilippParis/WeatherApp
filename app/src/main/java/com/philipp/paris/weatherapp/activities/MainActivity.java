package com.philipp.paris.weatherapp.activities;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Address;
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
import com.philipp.paris.weatherapp.components.fragments.DashBoardFragment;
import com.philipp.paris.weatherapp.components.dialogs.LocationDialogFragment;
import com.philipp.paris.weatherapp.components.fragments.ForecastFragment;
import com.philipp.paris.weatherapp.components.fragments.MeasurementsFragment;
import com.philipp.paris.weatherapp.domain.Settings;
import com.philipp.paris.weatherapp.service.LocationService;
import com.philipp.paris.weatherapp.service.ServiceCallback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationService.LocationServiceCallback {
    private DrawerLayout drawer;
    private Fragment currentFragment;
    private MenuItem itemSwitchLocation;
    private LocationService locationService = LocationService.getInstance();

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

        Settings settings = new Settings(getApplicationContext());
        if (!settings.showHomeLocationData()) {
            cacheLocationAndUpdateUI();
        } else {
            setTitle(R.string.home);
        }
    }

    private void refreshCurrentFragment() {
        getFragmentManager().beginTransaction().detach(currentFragment)
                .attach(currentFragment).commit();
    }

    private void cacheLocationAndUpdateUI() {
        // reset current location
        Settings settings = new Settings(getApplicationContext());
        settings.deleteCurrentLocation();
        settings.persist();

        locationService.getAddress(getApplicationContext(), new ServiceCallback<Address>() {
            @Override
            public void onSuccess(Address data) {
                Settings settings = new Settings(getApplicationContext());
                settings.setCurrentLocation(data.getLatitude(), data.getLongitude());
                settings.persist();
                refreshCurrentFragment();
                setTitle(data.getLocality());
            }

            @Override
            public void onError(Throwable t) {

            }
        }, this);
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
        itemSwitchLocation = menu.findItem(R.id.action_switch_location);
        itemSwitchLocation.setIcon(settings.showHomeLocationData() ? R.drawable.ic_menu_home : R.drawable.ic_location);
        itemSwitchLocation.setTitle(settings.showHomeLocationData() ? R.string.menu_show_gps : R.string.menu_show_home);
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

            // cache location in settings
            if (!settings.showHomeLocationData()) {
                cacheLocationAndUpdateUI();
            } else {
                setTitle(R.string.home);
            }
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
                fragment = ForecastFragment.newInstance();
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

            if (itemSwitchLocation != null) {
                itemSwitchLocation.setVisible(item.getItemId() == R.id.nav_dashboard);
            }
        }

        drawer.closeDrawers();
        return true;
    }

    @Override
    public void locationDisabled() {
        locationService.openLocationSettings(getApplicationContext());
    }

    @Override
    public void insufficientPermissions() {
        locationService.requestPermissions(this);
    }
}
