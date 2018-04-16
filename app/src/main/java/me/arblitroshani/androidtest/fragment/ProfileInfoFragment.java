package me.arblitroshani.androidtest.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.extras.GlideApp;
import me.arblitroshani.androidtest.model.User;

public class ProfileInfoFragment extends Fragment {

    private ImageView ivProfilePicture;
    private TextView tvName, tvEmail, tvBday, tvPhone;

    public ProfileInfoFragment() {}

    public static ProfileInfoFragment newInstance() {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_info, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvBday = view.findViewById(R.id.tvBday);
        tvPhone = view.findViewById(R.id.tvPhone);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String photoPath = User.getHighResGmailPhotoUrl(currentUser.getPhotoUrl());
        GlideApp.with(this)
                .load(photoPath)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfilePicture);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                tvName.setText(user.getName().replaceAll(" ", "\n"));
                tvEmail.setText(user.getEmail());
                tvBday.setText(user.getBirthday());
                tvPhone.setText(user.getPhone());
            }
        });
    }

}
