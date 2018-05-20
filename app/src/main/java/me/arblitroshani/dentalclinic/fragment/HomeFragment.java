package me.arblitroshani.dentalclinic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.adapter.HomeAdapter;
import me.arblitroshani.dentalclinic.extra.Constants;
import me.arblitroshani.dentalclinic.model.HomeSection;

public class HomeFragment extends Fragment {

    @BindView(R.id.rvHome)
    RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public HomeFragment() {}

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HomeAdapter(getData());
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<HomeSection> getData() {
        // if user is not doctor, show user
        if (1 == 1) {
            return Constants.HomeSections.getUserSections();
        }
        return Constants.HomeSections.getUserSections();
    }

}