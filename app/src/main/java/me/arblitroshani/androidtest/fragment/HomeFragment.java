package me.arblitroshani.androidtest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.adapter.HomeAdapter;
import me.arblitroshani.androidtest.model.HomeSection;

public class HomeFragment extends Fragment {

    private List<HomeSection> myDataset;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public HomeFragment() {
        myDataset = new ArrayList<>();
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.rvHome);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        myDataset.clear();
        addData();

        mAdapter = new HomeAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void addData() {
        myDataset.add(new HomeSection(
                "Profile",
                "this is a subtitle",
                0,
                getResources().getColor(R.color.teal_blue),
                R.drawable.ic_account_circle_white_48dp)
        );
        myDataset.add(new HomeSection(
                "Appointments",
                "this is a subtitle",
                0,
                getResources().getColor(R.color.dark_slate_gray),
                R.drawable.ic_today_white_48dp)
        );
        myDataset.add(new HomeSection(
                "Treatments",
                "this is a subtitle",
                0,
                getResources().getColor(R.color.gunmetal),
                R.drawable.ic_receipt_white_48dp)
        );
        myDataset.add(new HomeSection(
                "Our services",
                "this is a subtitle",
                4,
                getResources().getColor(R.color.saffron),
                R.drawable.ic_dashboard_white_48dp)
        );
        myDataset.add(new HomeSection(
                "Clinic info",
                "this is a subtitle",
                0,
                getResources().getColor(R.color.giants_orange_light),
                R.drawable.ic_info_outline_white_48dp)
        );
    }

}