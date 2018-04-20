package me.arblitroshani.androidtest.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.adapter.FragmentAdapter;
import me.arblitroshani.androidtest.GlideApp;
import me.arblitroshani.androidtest.fragment.ServicesFragment;
import me.arblitroshani.androidtest.fragment.ShareFragment;
import me.arblitroshani.androidtest.model.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 123;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TextView tvEmail;
    private ImageView ivProfilePicture;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        //appBarLayout.setExpanded(false,  true);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager_main);

        View headerView = navigationView.getHeaderView(0);
        tvEmail = headerView.findViewById(R.id.tvEmail);
        ivProfilePicture = headerView.findViewById(R.id.ivProfilePicture);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        auth = FirebaseAuth.getInstance();
        if (isUserSignedIn()) {
            refreshNavigationDrawer();
        }

        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isUserSignedIn()) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.PhoneBuilder()
                                                    .setDefaultCountryIso("al")
                                                    .build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        });

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.tab_text_1));
        titles.add(getString(R.string.tab_text_2));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ServicesFragment.newInstance());
        fragments.add(ShareFragment.newInstance());

        viewPager.setOffscreenPageLimit(2);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(mFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // viewPager.addOnPageChangeListener(pageChangeListener);

        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
//            if (position == 2) {
//                fab.show();
//            } else {
//                fab.hide();
//            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_notifications) {

        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_clinic) {

        } else if (id == R.id.nav_signout) {
            if (isUserSignedIn()) {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                showSnackbar("Signed out!");
                                tvEmail.setText("Click to login");
                                GlideApp.with(getApplicationContext())
                                        .load(R.mipmap.ic_launcher_round)
                                        .into(ivProfilePicture);
                            }
                        });
            }
        }
        navigationView.getMenu().getItem(0).setChecked(true);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUserMetadata metadata = auth.getCurrentUser().getMetadata();
                if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                    // The user is new
                    Intent i = new Intent(MainActivity.this, CreateUserProfileActivity.class);
                    startActivity(i);
                    showSnackbar("Success");
                } else {
                    // This is an existing user
                    showSnackbar("Welcome back");
                    navigationView.getMenu().getItem(0).setChecked(true);
                    onNavigationItemSelected(navigationView.getMenu().getItem(0));
                }
                auth = FirebaseAuth.getInstance();
                refreshNavigationDrawer();
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                if (response == null) {  // User pressed back button
                    showSnackbar("Sign in cancelled");
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar("No internet connection");
                    return;
                }
                showSnackbar("Unknown error");
            }
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.drawer_layout), message, Snackbar.LENGTH_SHORT).show();
    }

    private boolean isUserSignedIn() {
        return auth.getCurrentUser() != null;
    }

    private void refreshNavigationDrawer() {
        FirebaseUser currentUser = auth.getCurrentUser();
        tvEmail.setText(currentUser.getDisplayName());

        String photoUrl = User.getHighResGmailPhotoUrl(currentUser.getPhotoUrl());
        GlideApp.with(this)
                .load(photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfilePicture);
    }
}
