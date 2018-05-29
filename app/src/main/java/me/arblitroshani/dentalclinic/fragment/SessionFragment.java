package me.arblitroshani.dentalclinic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.GlideApp;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.model.Session;

public class SessionFragment extends Fragment {

    private static final String SESSION_ID_KEY = "sessionId";

    private FirebaseStorage storage;
    private StorageReference storageRefSessions;

    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.tvDiagnosis)
    TextView tvDiagnosis;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;

    public SessionFragment() {}

    public static SessionFragment newInstance(Session sessionId) {
        SessionFragment fragment = new SessionFragment();
        Bundle args = new Bundle();
        args.putSerializable(SESSION_ID_KEY, sessionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session, null, false);
        ButterKnife.bind(this, view);
        storage = FirebaseStorage.getInstance();
        storageRefSessions = storage.getReference().child("sessions");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Session currentSession = (Session) getArguments().getSerializable(SESSION_ID_KEY);
        tvDescription.setText(currentSession.getDescription());
        tvDiagnosis.setText(currentSession.getDiagnosis());

        String photoUrl = currentSession.getPhotoUrl();
        if (photoUrl != null) {
            GlideApp.with(getContext())
                    .load(storageRefSessions.child(photoUrl))
                    .into(ivPhoto);
        }
    }

}