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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.fragment.HomeFragment;
import me.arblitroshani.androidtest.fragment.ServicesFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    private Map<String, Integer> itemIds;

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

        itemIds = new HashMap<>();
        itemIds.put("Services", R.id.nav_services);
        itemIds.put("Clinic", R.id.nav_clinic);

        replaceFragment(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (navigationView.getMenu().findItem(R.id.nav_home).isChecked()) {
                finish();
            } else {
                replaceFragment(R.id.nav_home);
            }
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

        String fragmentClassName;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentClassName = "Home";
        } else if (id == R.id.nav_services) {
            fragmentClassName = "Services";
        } else if (id == R.id.nav_clinic) {
            fragmentClassName = "Home";
        } else if (id == R.id.nav_settings) {
            fragmentClassName = "Home";
        } else if (id == R.id.nav_help) {
            fragmentClassName = "Home";
        } else {
            fragmentClassName = "Home";
        }

        replaceFragment(fragmentClassName);
        item.setChecked(true);
        setTitle(fragmentClassName);

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void replaceFragment(String className) {
        uncheckAllMenuItems();
        Fragment fragment;
        try {
            FragmentManager fm = getSupportFragmentManager();
            fragment = fm.findFragmentByTag(className);

            if (fragment != null) {
                fm.popBackStackImmediate(className, 0);
            } else {
                fragment = (Fragment) Class.forName("me.arblitroshani.androidtest.fragment." + className + "Fragment").newInstance();

                FragmentTransaction transaction = fm.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.flContent, fragment, className);

//                if (fm.getFragments() != null)
//                    transaction.addToBackStack(tag);

                transaction.commit();
            }
            setTitle(className);

            // if itemIds has Title use that, else use R.id.home
            int id;
            if (itemIds.containsKey(className)) {
                id = itemIds.get(className);
            } else {
                id = R.id.nav_home;
            }
            navigationView.getMenu().findItem(id).setChecked(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void replaceFragment(int id) {
        onNavigationItemSelected(navigationView.getMenu().findItem(id));
    }

    private void uncheckAllMenuItems() {
        int size = navigationView.getMenu().size();
        Log.d("DEBUG", "called. size is: " + size);
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }
}
