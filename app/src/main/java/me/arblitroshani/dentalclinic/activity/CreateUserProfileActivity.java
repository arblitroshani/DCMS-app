package me.arblitroshani.dentalclinic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.extra.Utility;
import me.arblitroshani.dentalclinic.model.User;

public class CreateUserProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvBday)
    TextView tvBday;
    @BindView(R.id.tvNationalId)
    TextView tvNationalId;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPhone)
    EditText etPhone;

    @BindView(R.id.bDone)
    SubmitButton bSubmit;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private DocumentReference userRef;

    private User incompleteUser;

    private static final String emailError = "Please enter email!";
    private static final String phoneError = "Please enter phone number!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Intent i = getIntent();
        incompleteUser = i.getParcelableExtra("incomplete_user");
        String nationalId = i.getStringExtra("incomplete_user_national_id");

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users").document(nationalId);

        // check if exists - field with nationalId
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // set uid
                    userRef.update("uid", incompleteUser.getUid());

                    // set shared prefs
                    Utility.setNationalIdSharedPreference(this, nationalId);
                    Utility.setLoggedInUser(this, incompleteUser);

                    finish();
                } else {
                }
            }
        });

        tvName.setText(incompleteUser.getFullName());
        tvBday.setText(incompleteUser.getBirthday());
        tvNationalId.setText(nationalId);

        prepopulateFirebaseUserFields();

        bSubmit.setOnClickListener((View view) -> {
            etEmail.setError(null);
            etPhone.setError(null);
            if (TextUtils.isEmpty(etEmail.getText())) {
                etEmail.setHint(emailError);
                etEmail.setError(emailError);
                bSubmit.reset();
            } else if (TextUtils.isEmpty(etPhone.getText())) {
                etPhone.setHint(phoneError);
                etPhone.setError(phoneError);
                bSubmit.reset();
            } else {
                incompleteUser.setEmail(etEmail.getText().toString());
                incompleteUser.setPhone(etPhone.getText().toString());
                userRef.set(incompleteUser).addOnCompleteListener(task -> {
                    bSubmit.doResult(true);
                    Utility.setNationalIdSharedPreference(this, nationalId);
                    Utility.setLoggedInUser(this, incompleteUser);
                    new Handler().postDelayed(() -> finish(), 600);
                });
            }
        });
    }

    private void prepopulateFirebaseUserFields() {
        // prepopulate non-null fields
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String phone = user.getPhoneNumber();
            if (phone == null) {
                // signed in using email
                String email = user.getEmail();
                etEmail.setText(email);
                etEmail.setEnabled(false);
                incompleteUser.setEmail(email);
            } else {
                // signed in using phone number
                etPhone.setText(phone);
                etPhone.setEnabled(false);
                incompleteUser.setPhone(phone);
            }
        }
    }

}
