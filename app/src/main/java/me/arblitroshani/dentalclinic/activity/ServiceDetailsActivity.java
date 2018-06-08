package me.arblitroshani.dentalclinic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.GlideApp;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.adapter.ServiceDoctorsAdapter;
import me.arblitroshani.dentalclinic.adapter.ServicePhotosAdapter;
import me.arblitroshani.dentalclinic.model.DoctorBasic;
import me.arblitroshani.dentalclinic.model.Service;

public class ServiceDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.tvSubtitle)
    TextView tvSubtitle;

    @BindView(R.id.image_scrolling_top)
    ImageView ivPicture;

    @BindView(R.id.rvDoctors)
    RecyclerView rvDoctors;

    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;

    @BindView(R.id.pager_indicator)
    IndefinitePagerIndicator ipi;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private List<DoctorBasic> myDataset;

    private RecyclerView.Adapter adapterDoctors, adapterPhotos;
    private RecyclerView.LayoutManager layoutManagerDoctors, layoutManagerPhotos;

    private FirebaseStorage storage;
    private StorageReference storageRefServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storage = FirebaseStorage.getInstance();
        storageRefServices = storage.getReference().child("services");

        rvDoctors.setHasFixedSize(true);
        layoutManagerDoctors = new LinearLayoutManager(this);

        rvPhotos.setHasFixedSize(true);
        layoutManagerPhotos = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        Intent i = getIntent();
        final Service currentService = (Service) i.getSerializableExtra("service_to_display");
        final String serviceId = i.getStringExtra("service_id");

        fab.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, currentService.getDescription());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("services").document(serviceId).collection("doctors")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) return; // listen failed

                    myDataset = snapshot.toObjects(DoctorBasic.class);

                    adapterDoctors = new ServiceDoctorsAdapter(myDataset);
                    rvDoctors.setLayoutManager(layoutManagerDoctors);
                    rvDoctors.setAdapter(adapterDoctors);

                    rvPhotos.setLayoutManager(layoutManagerPhotos);
                    rvPhotos.setNestedScrollingEnabled(false);
                    SnapHelper snapHelper = new PagerSnapHelper();

                    adapterPhotos = new ServicePhotosAdapter(currentService.getPhotoUrls(), serviceId);
                    rvPhotos.setAdapter(adapterPhotos);
                    snapHelper.attachToRecyclerView(rvPhotos);
                    ipi.attachToRecyclerView(rvPhotos);
                });

        // bind values
        getSupportActionBar().setTitle(currentService.getTitle());
        tvDescription.setText(currentService.getDescription());
        tvSubtitle.setText(currentService.getSubtitle());

        GlideApp.with(this)
                .load(storageRefServices.child(currentService.getPhotoUrl()))
                .into(ivPicture);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
