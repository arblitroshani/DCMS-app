package me.arblitroshani.dentalclinic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.microblink.activity.ScanActivity;
import com.microblink.activity.ScanCard;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.recognizers.blinkid.mrtd.MRTDRecognitionResult;
import com.microblink.recognizers.blinkid.mrtd.MRTDRecognizerSettings;
import com.microblink.recognizers.settings.RecognitionSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.microblink.results.ocr.OcrResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.activity.CreateUserProfileActivity;
import me.arblitroshani.dentalclinic.adapter.TreatmentsAdapter;
import me.arblitroshani.dentalclinic.extra.Config;
import me.arblitroshani.dentalclinic.extra.Utility;
import me.arblitroshani.dentalclinic.model.Treatment;
import me.arblitroshani.dentalclinic.model.User;

public class PatientsFragment extends Fragment {

    public static final int MY_REQUEST_CODE = 0x101;

    private List<Treatment> myDataset;
    private List<String> treatmentIds;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseFirestore db;

    @BindView(R.id.tv_card_name)
    TextView tvName;
    @BindView(R.id.rvTreatments)
    RecyclerView rvTreatments;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    public PatientsFragment() {}

    public static PatientsFragment newInstance() {
        PatientsFragment fragment = new PatientsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patients, null, false);
        ButterKnife.bind(this, view);
        db = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Intent intent = new Intent(this.getContext(), ScanCard.class);
        intent.putExtra(ScanCard.EXTRAS_LICENSE_KEY, Config.BLINKID_LICENSE_KEY);

        RecognitionSettings settings = new RecognitionSettings();
        settings.setRecognizerSettingsArray(setupSettingsArray());
        intent.putExtra(ScanCard.EXTRAS_RECOGNITION_SETTINGS, settings);
        intent.putExtra(ScanActivity.EXTRAS_BEEP_RESOURCE, R.raw.beep);

        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == ScanCard.RESULT_OK && data != null) {
                RecognitionResults result = data.getParcelableExtra(ScanCard.EXTRAS_RECOGNITION_RESULTS);
                onScanningDone(result);
            }
        }
    }

    private void onScanningDone(RecognitionResults results) {
        BaseRecognitionResult[] dataArray = results.getRecognitionResults();

        for (BaseRecognitionResult baseResult : dataArray) {
            if (baseResult instanceof MRTDRecognitionResult) {
                MRTDRecognitionResult result = (MRTDRecognitionResult) baseResult;

                if(result.isValid() && !result.isEmpty()) {
                    if(result.isMRZParsed()) {
//                        // read data
//                        User incompleteUser = new User(
//                                Utility.toCamelCase(result.getSecondaryId()),
//                                Utility.toCamelCase(result.getPrimaryId()),
//                                "no_email",
//                                "no_phone",
//                                getBirthday(result.getRawDateOfBirth()),
//                                result.getSex(),
//                                result.getDocumentNumber(),
//                                user.getUid(),
//                                result.getNationality(),
//                                User.TYPE_USER
//                        );
//
                        String nationalId = result.getOpt1().substring(0, 10);

                        tvName.setText(
                                Utility.toCamelCase(result.getSecondaryId()) +
                                " " +
                                Utility.toCamelCase(result.getPrimaryId()));
                        // load from firestore all treatments of user with id nationalId
                        rvTreatments.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(getActivity());
                        rvTreatments.setLayoutManager(layoutManager);

                        db.collection("treatments")
                                .whereEqualTo("nationalId", nationalId)
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
                                        rvTreatments.setAdapter(adapter);
                                    }
                                });
                    } else {
                        OcrResult rawOcr = result.getOcrResult();
                        // attempt to parse OCR result by yourself
                        // or ask user to try again
                    }
                } else {
                    Toast.makeText(this.getContext(), "Unsuccessful scanning", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private RecognizerSettings[] setupSettingsArray() {
        MRTDRecognizerSettings sett = new MRTDRecognizerSettings();
        return new RecognizerSettings[] { sett };
    }

}