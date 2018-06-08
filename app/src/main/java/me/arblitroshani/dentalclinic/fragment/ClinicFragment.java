package me.arblitroshani.dentalclinic.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.adapter.ServiceDoctorsAdapter;
import me.arblitroshani.dentalclinic.adapter.ServicePhotosAdapter;
import me.arblitroshani.dentalclinic.model.DoctorBasic;
import me.arblitroshani.dentalclinic.model.User;

public class ClinicFragment extends Fragment {

    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.rvDoctors)
    RecyclerView rvDoctors;

    private List<DoctorBasic> myDataset;

    private RecyclerView.Adapter adapterDoctors;
    private RecyclerView.LayoutManager layoutManagerDoctors;

    public ClinicFragment() {}

    public static ClinicFragment newInstance() {
        ClinicFragment fragment = new ClinicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clinic, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // onclick location
        tvLocation.setOnClickListener(view1 -> {
            Uri gmmIntentUri = Uri.parse("geo:41.3297,19.8138");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        });

        // show doctors list
        rvDoctors.setHasFixedSize(true);
        layoutManagerDoctors = new LinearLayoutManager(this.getContext());
        rvDoctors.setLayoutManager(layoutManagerDoctors);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("type", "doctor")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) return; // listen failed
                    myDataset = new ArrayList<>();

                    List<User> currUser = snapshot.toObjects(User.class);

                    for (int i = 0; i < currUser.size(); i++)
                        myDataset.add(new DoctorBasic(currUser.get(i).getFullName(), null));

                    adapterDoctors = new ServiceDoctorsAdapter(myDataset);
                    rvDoctors.setAdapter(adapterDoctors);
                });
    }

}