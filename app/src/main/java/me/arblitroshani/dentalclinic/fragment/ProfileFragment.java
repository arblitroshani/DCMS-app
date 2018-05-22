package me.arblitroshani.dentalclinic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.GlideApp;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.activity.MainActivity;
import me.arblitroshani.dentalclinic.extra.PhotoFullPopupWindow;
import me.arblitroshani.dentalclinic.model.User;

public class ProfileFragment extends Fragment {

    @BindView(R.id.ivProfilePicture)
    ImageView ivProfilePicture;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvBday)
    TextView tvBday;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout container;
    @BindView(R.id.shimmer_view_name)
    ShimmerFrameLayout nameContainer;

    public ProfileFragment() {}

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).collapseAppBar();

        container.startShimmerAnimation();
        nameContainer.startShimmerAnimation();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        final String photoPath = User.getHighResGmailPhotoUrl(currentUser.getPhotoUrl());
        GlideApp.with(this)
                .load(photoPath)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfilePicture);

        ivProfilePicture.setOnClickListener(view1 ->
                new PhotoFullPopupWindow(getContext(), R.layout.popup_photo_full, view1, photoPath, null));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            container.stopShimmerAnimation();
            nameContainer.stopShimmerAnimation();
            tvName.setText(user.getFullName());
            tvEmail.setText(user.getEmail());
            tvBday.setText(user.getBirthday());
            tvPhone.setText(user.getPhone());
        });
    }

}