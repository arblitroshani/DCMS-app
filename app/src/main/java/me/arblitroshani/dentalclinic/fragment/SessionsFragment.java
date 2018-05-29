package me.arblitroshani.dentalclinic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.adapter.SessionsAdapter;
import me.arblitroshani.dentalclinic.model.Session;

public class SessionsFragment extends Fragment {

    private static final String TREATMENT_ID_KEY = "treatmentId";

    private List<Session> myDataset;

    @BindView(R.id.frameLayout)
    FrameLayout flServices;
    @BindView(R.id.rvSessions)
    RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseFirestore db;

    public SessionsFragment() {}

    public static SessionsFragment newInstance(String treatmentId) {
        SessionsFragment fragment = new SessionsFragment();
        Bundle args = new Bundle();
        args.putString(TREATMENT_ID_KEY, treatmentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treatment, null, false);
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
                .document(getArguments().getString(TREATMENT_ID_KEY))
                .collection("sessions")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) return;
                    if (snapshot != null) {
                        myDataset = snapshot.toObjects(Session.class);
                        adapter = new SessionsAdapter(myDataset);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

}