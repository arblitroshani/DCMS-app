package me.arblitroshani.androidtest.activity;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.adapter.FragmentAdapter;
import me.arblitroshani.androidtest.fragment.ServicesFragment;
import me.arblitroshani.androidtest.fragment.ShareFragment;

public class UserProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My profile");

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.container);

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.tab_text_3));
        titles.add(getString(R.string.tab_text_4));
        titles.add(getString(R.string.tab_text_5));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ShareFragment.newInstance());
        fragments.add(ShareFragment.newInstance());
        fragments.add(ShareFragment.newInstance());

        viewPager.setOffscreenPageLimit(2);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(mFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
