package me.arblitroshani.dentalclinic.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.extra.Utility;
import me.arblitroshani.dentalclinic.model.Availability;

public class AvailabilityFragment extends Fragment {

    @BindView(R.id.cvInfo)
    CardView cvNotice;
    @BindView(R.id.bDone)
    SubmitButton sbSubmit;
    @BindView(R.id.etNotes)
    EditText etNotes;

    private FirebaseFirestore db;

    @BindView(R.id.chb1) CheckBox chb0;
    @BindView(R.id.chb2) CheckBox chb1;
    @BindView(R.id.chb3) CheckBox chb2;
    @BindView(R.id.chb4) CheckBox chb3;
    @BindView(R.id.chb5) CheckBox chb4;
    @BindView(R.id.chb6) CheckBox chb5;

    public AvailabilityFragment() {}

    public static AvailabilityFragment newInstance() {
        AvailabilityFragment fragment = new AvailabilityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_availability, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Toast.makeText(this.getContext(), "FOR DEMO: only editable on Tuesday", Toast.LENGTH_LONG).show();

        db = FirebaseFirestore.getInstance();

        String nationalId = Utility.getNationalIdSharedPreference(this.getContext());
        String doctorName = Utility.getLoggedInUser(this.getContext()).getFullName();

        db.collection("availability")
                .document(nationalId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Availability av = documentSnapshot.toObject(Availability.class);
                    if (av != null) {
                        etNotes.setText(av.getNote());
                        List<Boolean> availability = av.getAvailability();
                        chb0.setChecked(availability.get(0));
                        chb0.setChecked(availability.get(0));
                        chb1.setChecked(availability.get(1));
                        chb2.setChecked(availability.get(2));
                        chb3.setChecked(availability.get(3));
                        chb4.setChecked(availability.get(4));
                        chb5.setChecked(availability.get(5));
                    } else {
                        // create default av and upload
                        Availability defaultAvailability = new Availability(
                                Calendar.getInstance().getTime(),
                                "",
                                doctorName,
                                new ArrayList<>(Arrays.asList(
                                        true, true, true,
                                        true, true, true
                                ))
                        );
                        db.collection("availability")
                                .document(nationalId)
                                .set(defaultAvailability);
                    }

                });

        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            sbSubmit.setOnClickListener(view1 -> {
                db.collection("availability").document(nationalId)
                        .set(new Availability(
                                Calendar.getInstance().getTime(),
                                etNotes.getText().toString(),
                                doctorName,
                                new ArrayList<>(Arrays.asList(
                                    chb0.isChecked(),
                                    chb1.isChecked(),
                                    chb2.isChecked(),
                                    chb3.isChecked(),
                                    chb4.isChecked(),
                                    chb5.isChecked()
                        ))))
                        .addOnSuccessListener(documentReference -> {
                            sbSubmit.doResult(true);
                            new Handler().postDelayed(() -> {
                                this.getActivity().onBackPressed();
                            }, 600);
                        });
            });
        } else {
            cvNotice.setVisibility(View.VISIBLE);
            sbSubmit.setVisibility(View.GONE);
            etNotes.setEnabled(false);

            chb0.setClickable(false);
            chb1.setClickable(false);
            chb2.setClickable(false);
            chb3.setClickable(false);
            chb4.setClickable(false);
            chb5.setClickable(false);
        }
    }

}