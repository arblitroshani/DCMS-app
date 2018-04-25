package me.arblitroshani.androidtest.fragment;

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

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import me.arblitroshani.androidtest.adapter.ServicesAdapter;
import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.model.Service;

public class ServicesFragment extends Fragment {

    private List<Service> myDataset;
    private List<String> serviceIds;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ServicesFragment() {}

    public static ServicesFragment newInstance() {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_services, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // set animation here
        Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_about_card_show);
        FrameLayout flServices = view.findViewById(R.id.frameLayout);
        flServices.startAnimation(animation);

        mRecyclerView = view.findViewById(R.id.rvServices);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("services")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {
                        if (e != null) return; // listen failed
                        if (snapshot != null) {
//                            myDataset = snapshot.toObjects(Service.class);
//                            mAdapter = new ServicesAdapter(myDataset);
//
                            myDataset = new ArrayList<>();
                            serviceIds = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : snapshot) {
                                myDataset.add(doc.toObject(Service.class));
                                serviceIds.add(doc.getId());
                            }
                            mAdapter = new ServicesAdapter(myDataset, serviceIds);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });
    }

}