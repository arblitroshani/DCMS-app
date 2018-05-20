package me.arblitroshani.dentalclinic.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.GlideApp;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.extra.Utility;
import me.arblitroshani.dentalclinic.fragment.HomeFragment;
import me.arblitroshani.dentalclinic.fragment.SessionsFragment;
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

    private TextView tvName;
    private TextView tvEmail;
    private ImageView ivProfile;

    @BindView(R.id.image_scrolling_top)
    ImageView ivLogo;

    private Map<String, Integer> itemIds;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageRefServices;

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

        // load image from storage in toolbar
        storage = FirebaseStorage.getInstance();
        storageRefServices = storage.getReference().child("branding");

        View headerView = navigationView.getHeaderView(0);
        ivProfile = headerView.findViewById(R.id.imageView);
        tvName = headerView.findViewById(R.id.tvName);
        tvEmail = headerView.findViewById(R.id.tvEmail);

        GlideApp.with(this)
                .load(storageRefServices.child("tooth.png"))
                .into(ivLogo);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        collapseAppBar();
    }

    private void updateRegistrationToken() {
        MyFirebaseInstanceIDService myFirebaseInstanceIdService = new MyFirebaseInstanceIDService();
        Intent intent = new Intent(getApplicationContext(), myFirebaseInstanceIdService.getClass());
        startService(intent); //invoke onCreate

        Map<String, String> m = new HashMap<>();
        m.put("value", Utility.getFirebaseInstanceId(getApplicationContext()));

        FirebaseFirestore.getInstance()
                .collection("registrationTokens")
                .document(currentUser.getUid())
                .set(m);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            invalidateOptionsMenu();
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
                    //replaceFragment(R.id.nav_home);
                }
                auth = FirebaseAuth.getInstance();
                refreshNavigationDrawer();
                updateRegistrationToken();
                drawer.closeDrawer(GravityCompat.START);
                replaceFragment(R.id.nav_home);
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
                                .load(R.drawable.default_profile)
                                .apply(RequestOptions.circleCropTransform())
                                .into(ivProfile);
                        setupMenuLoggedIn(false);
                        replaceFragment(R.id.nav_home);
                        invalidateOptionsMenu();
                    });
        }
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
            fragmentClassName = "Home";
        } else if (id == R.id.nav_settings) {
            fragmentClassName = "Home";
        } else if (id == R.id.nav_help) {
            fragmentClassName = "Home";
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

    public void replaceFragment(String className, String optionalTreatmentId) {
        uncheckAllMenuItems();
        Fragment fragment;
        try {
            FragmentManager fm = getSupportFragmentManager();
            fragment = fm.findFragmentByTag(className);

            if (fragment != null) {
                fm.popBackStackImmediate(className, 0);
            } else {
                if (optionalTreatmentId.equals(DEFAULT_TREATMENT_ID) || !className.equals("Sessions")) {
                    fragment = (Fragment) Class.forName("me.arblitroshani.dentalclinic.fragment." + className + "Fragment").newInstance();
                } else {
                    fragment = SessionsFragment.newInstance(optionalTreatmentId);
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

    public void replaceFragment(int id) {
        onNavigationItemSelected(navigationView.getMenu().findItem(id));
    }

    public void startActivity(String className) {
        Intent i;
        switch (className) {
            case "AppointmentsActivity":
                i = new Intent(MainActivity.this, AppointmentsActivity.class);
                break;
            default:
                i = new Intent(MainActivity.this, MainActivity.class);
                break;
        }
        startActivity(i);
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
        tvName.setText(currentUser.getDisplayName());
        tvEmail.setText(currentUser.getEmail());

        String photoUrl = User.getHighResGmailPhotoUrl(currentUser.getPhotoUrl());
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
