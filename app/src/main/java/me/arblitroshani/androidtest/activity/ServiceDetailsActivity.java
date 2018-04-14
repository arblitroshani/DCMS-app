package me.arblitroshani.androidtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.extras.GlideApp;
import me.arblitroshani.androidtest.model.Service;

public class ServiceDetailsActivity extends AppCompatActivity {

    private TextView tvDescription;
    private TextView tvSubtitle;
    private ImageView ivPicture;

    private FirebaseStorage storage;
    private StorageReference storageRefServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        storage = FirebaseStorage.getInstance();
        storageRefServices = storage.getReference().child("services");

        tvDescription = findViewById(R.id.tvDescription);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        ivPicture = findViewById(R.id.image_scrolling_top);

        Intent i = getIntent();
        Service currentService = i.getParcelableExtra("service_to_display");

        // bind values
        getSupportActionBar().setTitle(currentService.getTitle());
        tvDescription.setText(currentService.getDescription());
        tvSubtitle.setText(currentService.getSubtitle());

        GlideApp.with(this)
                .load(storageRefServices.child(currentService.getPhotoUrl()))
                .into(ivPicture);
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
