package me.arblitroshani.androidtest.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.fragment.HomeFragment;
import me.arblitroshani.androidtest.fragment.ServicesFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        selectTab(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (navigationView.getMenu().findItem(R.id.nav_home).isChecked()) {
                finish();
            }
            selectTab(R.id.nav_home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment;
        Class fragmentClass;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_profile) {
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_appointments) {
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_treatments) {
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_services) {
            fragmentClass = ServicesFragment.class;
        } else if (id == R.id.nav_clinic) {
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_settings) {
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_help) {
            fragmentClass = HomeFragment.class;
        } else {
            fragmentClass = HomeFragment.class;
        }

        String tag = String.valueOf(item.getTitle());
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentByTag(tag);

        if (fragment != null) {
            fm.popBackStackImmediate(tag, 0);
        } else {
            try {
                fragment = (Fragment) fragmentClass.newInstance();

                FragmentTransaction transaction = fm.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.flContent, fragment, tag);

//                if (fm.getFragments() != null)
//                    transaction.addToBackStack(tag);

                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        uncheckAllMenuItems();
        item.setChecked(true);
        setTitle(tag);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void selectTab(int position) {
        uncheckAllMenuItems();
        MenuItem item = navigationView.getMenu().findItem(position);
        item.setChecked(true);
        onNavigationItemSelected(item);
    }

    private void uncheckAllMenuItems() {
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }
}
