package me.arblitroshani.dentalclinic.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.GlideApp;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.extra.Utility;
import me.arblitroshani.dentalclinic.fragment.HomeFragment;
import me.arblitroshani.dentalclinic.fragment.SessionFragment;
import me.arblitroshani.dentalclinic.fragment.SessionsFragment;
import me.arblitroshani.dentalclinic.model.Session;
import me.arblitroshani.dentalclinic.model.User;
import me.arblitroshani.dentalclinic.service.MyFirebaseInstanceIDService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 123;
    private static final String DEFAULT_TREATMENT_ID = "default";

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.abl)
    AppBarLayout abl;
    @BindView(R.id.ctl)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.image_scrolling_top)
    ImageView ivLogo;

    private TextView tvName;
    private TextView tvEmail;
    private ImageView ivProfile;

    private Map<String, Integer> itemIds;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        ivProfile = headerView.findViewById(R.id.imageView);
        tvName = headerView.findViewById(R.id.tvName);
        tvEmail = headerView.findViewById(R.id.tvEmail);

        setupMenuLoggedIn(false);

        auth = FirebaseAuth.getInstance();
        if (isUserSignedIn()) {
            refreshNavigationDrawer();
            updateRegistrationToken(); 
        }

        itemIds = new HashMap<>();
        itemIds.put("Services", R.id.nav_services);
        itemIds.put("Clinic", R.id.nav_clinic);

        replaceFragment(R.id.nav_home);

        collapseAppBar();
    }

    private void updateRegistrationToken() {
        MyFirebaseInstanceIDService myFirebaseInstanceIdService = new MyFirebaseInstanceIDService();
        Intent intent = new Intent(getApplicationContext(), myFirebaseInstanceIdService.getClass());
        startService(intent);

        Map<String, String> m = new HashMap<>();
        m.put("value", Utility.getFirebaseInstanceId(getApplicationContext()));

        try {
            FirebaseFirestore.getInstance()
                    .collection("registrationTokens")
                    .document(currentUser.getUid())
                    .set(m);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            invalidateOptionsMenu();
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null) {
                    auth.getCurrentUser().reload();
                    FirebaseUserMetadata metadata = auth.getCurrentUser().getMetadata();
                    if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                        // New user
                        Intent i = new Intent(MainActivity.this, IdCardScanActivity.class);
                        startActivity(i);
                        refreshNavigationDrawer();
                    } else {
                        // Existing user
                        showSnackbar("Welcome back");
                        FirebaseFirestore.getInstance()
                                .collection("users")
                                .whereEqualTo("uid", auth.getCurrentUser().getUid())
                                .get()
                                .addOnCompleteListener(task -> {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Utility.setNationalIdSharedPreference(MainActivity.this, document.getId());
                                        Utility.setLoggedInUser(MainActivity.this, document.toObject(User.class));
                                        replaceFragment(R.id.nav_home);
                                    }
                                    refreshNavigationDrawer();
                                });
                    }
                    updateRegistrationToken();
                    drawer.closeDrawer(GravityCompat.START);
                    replaceFragment(R.id.nav_home);
                }
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

    public void login() {
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

    private void logout() {
        if (isUserSignedIn()) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        showSnackbar("Signed out!");
                        tvName.setText("");
                        tvEmail.setText("");
                        GlideApp.with(getApplicationContext())
                                .load(R.drawable.member_default)
                                .apply(RequestOptions.circleCropTransform())
                                .into(ivProfile);
                        setupMenuLoggedIn(false);
                        //replaceFragment(R.id.nav_home);
                        invalidateOptionsMenu();
                        deleteInstanceId();
                        Utility.setNationalIdSharedPreference(this, null);
                        Utility.setLoggedInUser(this, null);
                        replaceFragment(R.id.nav_home);
                    });
        }
    }

    private static void deleteInstanceId() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {}
        }.execute();
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.drawer_layout), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.findFragmentByTag("Sessions") instanceof SessionsFragment) {
                if (Utility.getLoggedInUser(this.getBaseContext()).getType().equals(User.TYPE_DOCTOR))
                    replaceFragment("Patients");
                else
                    replaceFragment("Treatments");
            } else if (fm.findFragmentByTag("Home") instanceof HomeFragment) {
                finish();
            } else {
                replaceFragment(R.id.nav_home);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (isUserSignedIn()) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_in) {
            login();
            return true;
        } else if (id == R.id.action_sign_out) {
            logout();
            return true;
        } else if (id == R.id.action_force_crash) {
            Crashlytics.getInstance().crash();
        } else if (id == R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        String fragmentClassName;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentClassName = "Home";
        } else if (id == R.id.nav_services) {
            fragmentClassName = "Services";
        } else if (id == R.id.nav_clinic) {
            fragmentClassName = "Clinic";
        } else if (id == R.id.nav_settings) {
            startActivity("SettingsActivity");
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_sign_in) {
            login();
            return true;
        } else if (id == R.id.nav_sign_out) {
            logout();
            return true;
        } else {
            fragmentClassName = "Home";
        }

        drawer.closeDrawer(GravityCompat.START);
        replaceFragment(fragmentClassName);
        return true;
    }

    public void replaceFragment(String className, Object optionalId) {
        uncheckAllMenuItems();
        Fragment fragment;
        try {
            FragmentManager fm = getSupportFragmentManager();
            fragment = fm.findFragmentByTag(className);

            if (fragment != null) {
                if (className.equals("Home")) {
                    HomeFragment hf = (HomeFragment) fragment;
                    hf.setAdapter();
                }
                fm.popBackStackImmediate(className, 0);
            } else {
                if (optionalId instanceof Session) {
                    fragment = SessionFragment.newInstance(((Session)optionalId));
                } else {
                    String optionalIdString = (String) optionalId;
                    if (optionalIdString.equals(DEFAULT_TREATMENT_ID)) {
                        fragment = (Fragment) Class.forName("me.arblitroshani.dentalclinic.fragment." + className + "Fragment").newInstance();
                    } else {
                        fragment = SessionsFragment.newInstance(optionalIdString);
                    }
                }

                FragmentTransaction transaction = fm.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.flContent, fragment, className);

//                if (fm.getFragments() != null)
//                    transaction.addToBackStack(tag);

                transaction.commit();
            }
            collapsingToolbarLayout.setTitle(className);

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

    public void replaceFragment(String className) {
        replaceFragment(className, DEFAULT_TREATMENT_ID);
    }

    public void replaceFragment(int id) { // TODO: consider using above method
        onNavigationItemSelected(navigationView.getMenu().findItem(id));
    }

    public void startActivity(String className) {
        Class toOpen;
        switch (className) {
            case "AppointmentsActivity":
                toOpen = AppointmentsActivity.class;
                break;
            case "SettingsActivity":
                toOpen = SettingsActivity.class;
                break;
            default:
                toOpen = MainActivity.class;
                break;
        }
        startActivity(new Intent(MainActivity.this, toOpen));
    }

    private void uncheckAllMenuItems() {
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    private boolean isUserSignedIn() {
        return auth.getCurrentUser() != null;
    }

    private void refreshNavigationDrawer() {
        setupMenuLoggedIn(true);

        currentUser = auth.getCurrentUser();
        User user = Utility.getLoggedInUser(this);

        if (user != null) {
            tvName.setText(user.getFullName());
            tvEmail.setText(user.getEmail());
        } else {
            tvName.setText(currentUser.getDisplayName());
            tvEmail.setText(currentUser.getEmail());
        }

        String photoUrl = User.getHighResGmailPhotoUrl(currentUser.getPhotoUrl());
        Log.i("tag-not", "photoUrl: " + photoUrl);
        GlideApp.with(this)
                .load(photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfile);
    }

    private void setupMenuLoggedIn(boolean b) {
        Menu navMenu = navigationView.getMenu();
        navMenu.findItem(R.id.nav_sign_in).setVisible(!b);
        navMenu.findItem(R.id.nav_sign_out).setVisible(b);
    }

    public void collapseAppBar() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> abl.setExpanded(false, true), 3000);
    }
}
