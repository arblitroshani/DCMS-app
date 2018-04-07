package me.arblitroshani.androidtest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ServicesFragment extends Fragment {

    private static ArrayList<String> myDataset = null;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ServicesFragment() {
        // Required empty public constructor
    }

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
        mRecyclerView = view.findViewById(R.id.rvServices);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (myDataset == null) {
            myDataset = new ArrayList<>();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("services")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    myDataset.add(document.getData().get("title").toString());
                                }
                                mAdapter = new MyAdapter(myDataset);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }
                    });
        } else {
            mAdapter = new MyAdapter(myDataset);
            mRecyclerView.setAdapter(mAdapter);
        }

    }

}
