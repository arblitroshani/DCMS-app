package me.arblitroshani.dentalclinic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.adapter.ServicesAdapter;
import me.arblitroshani.dentalclinic.adapter.TreatmentsAdapter;
import me.arblitroshani.dentalclinic.model.Service;
import me.arblitroshani.dentalclinic.model.Treatment;

public class TreatmentsFragment extends Fragment {

    private List<Treatment> myDataset;
    private List<String> treatmentIds;

    @BindView(R.id.frameLayout)
    FrameLayout flServices;
    @BindView(R.id.rvTreatments)
    RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseFirestore db;

    public TreatmentsFragment() {}

    public static TreatmentsFragment newInstance() {
        TreatmentsFragment fragment = new TreatmentsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treatments, null, false);
        ButterKnife.bind(this, view);
        db = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        db.collection("treatments")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) return;
                    if (snapshot != null) {
                        myDataset = new ArrayList<>();
                        treatmentIds = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshot) {
                            myDataset.add(doc.toObject(Treatment.class));
                            treatmentIds.add(doc.getId());
                        }
                        adapter = new TreatmentsAdapter(myDataset, treatmentIds);
                        recyclerView.setAdapter(adapter);
                    }
                });

    }

}