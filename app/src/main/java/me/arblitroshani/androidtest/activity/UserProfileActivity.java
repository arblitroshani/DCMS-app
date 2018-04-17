package me.arblitroshani.androidtest.activity;

import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.adapter.FragmentAdapter;
import me.arblitroshani.androidtest.fragment.ProfileInfoFragment;
import me.arblitroshani.androidtest.fragment.ShareFragment;

public class UserProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;

    private FragmentAdapter mFragmentAdapter;
    private ProfileInfoFragment profileInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My profile");

        fab = findViewById(R.id.fab);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.container);

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.tab_text_3));
        titles.add(getString(R.string.tab_text_4));

        List<Fragment> fragments = new ArrayList<>();
        profileInfoFragment = ProfileInfoFragment.newInstance();
        fragments.add(profileInfoFragment);
        fragments.add(ShareFragment.newInstance());

        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(pageChangeListener);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(mFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get current values
                openDialog();
            }
        });
    }

    private void openDialog() {
        LayoutInflater inflater = LayoutInflater.from(UserProfileActivity.this);
        View subView = inflater.inflate(R.layout.dialog_editprofileinfo, null);

        final EditText etDialogBday = subView.findViewById(R.id.etBday);
        final EditText etDialogPhone = subView.findViewById(R.id.etPhone);

        new AlertDialog.Builder(UserProfileActivity.this)
                .setTitle("Edit profile info")
                .setView(subView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                fab.show();
            } else {
                fab.hide();
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {}
    };

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
