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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.GlideApp;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.activity.MainActivity;
import me.arblitroshani.dentalclinic.extra.PhotoFullPopupWindow;
import me.arblitroshani.dentalclinic.extra.Utility;
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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        final String photoPath = User.getHighResGmailPhotoUrl(currentUser.getPhotoUrl());
        GlideApp.with(this)
                .load(photoPath)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfilePicture);

        ivProfilePicture.setOnClickListener(view1 ->
                new PhotoFullPopupWindow(getContext(), R.layout.popup_photo_full, view1, photoPath, null));

        String nationalId = Utility.getNationalIdSharedPreference(this.getActivity());

        if (nationalId != null) {
            User user = Utility.getLoggedInUser(this.getContext());
            tvName.setText(getFullNameCamelCase(user.getName(), user.getSurname()));
            tvEmail.setText(user.getEmail());
            tvBday.setText(user.getBirthday());
            tvPhone.setText(user.getPhone());
        }
    }

    private String getFullNameCamelCase(String name, String surname) {
        name = name.charAt(0) + name.substring(1).toLowerCase();
        surname = surname.charAt(0) + surname.substring(1).toLowerCase();
        return name + " " + surname;
    }

}