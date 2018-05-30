package me.arblitroshani.dentalclinic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.adapter.HomeAdapter;
import me.arblitroshani.dentalclinic.extra.Constants;
import me.arblitroshani.dentalclinic.extra.Utility;
import me.arblitroshani.dentalclinic.model.HomeSection;
import me.arblitroshani.dentalclinic.model.User;

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

        setAdapter();
    }

    private List<HomeSection> getData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        User currentUser = Utility.getLoggedInUser(this.getContext());

        if (user != null && currentUser != null && currentUser.getType().equals(User.TYPE_DOCTOR)) {
            return Constants.HomeSections.getDoctorSections();
        }
        return Constants.HomeSections.getUserSections();
    }

    public void setAdapter() {
        mAdapter = new HomeAdapter(getData());
        mRecyclerView.setAdapter(mAdapter);
    }

}