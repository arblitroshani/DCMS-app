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

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.adapter.ServicesAdapter;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.model.Service;

public class ServicesFragment extends Fragment {

    private List<Service> myDataset;
    private List<String> serviceIds;

    @BindView(R.id.frameLayout)
    FrameLayout flServices;
    @BindView(R.id.rvServices)
    RecyclerView mRecyclerView;

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
        View view = inflater.inflate(R.layout.fragment_services, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_about_card_show);
        flServices.startAnimation(animation);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("services")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) return; // listen failed
                    if (snapshot != null) {
                        myDataset = new ArrayList<>();
                        serviceIds = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshot) {
                            myDataset.add(doc.toObject(Service.class));
                            serviceIds.add(doc.getId());
                        }
                        mAdapter = new ServicesAdapter(myDataset, serviceIds);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
    }

}