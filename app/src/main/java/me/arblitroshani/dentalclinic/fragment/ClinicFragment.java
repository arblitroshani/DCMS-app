package me.arblitroshani.dentalclinic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.arblitroshani.dentalclinic.R;

public class ClinicFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_clinic, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


    }

}