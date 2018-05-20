package me.arblitroshani.dentalclinic.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.model.User;

public class CreateUserProfileActivity extends AppCompatActivity {

    private String birthday;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.dpBirthday)
    DatePicker dpBirthday;

    @BindView(R.id.etPhone)
    EditText etPhone;

    @BindView(R.id.bDone)
    Button bDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // prepopulate firebaseuser nonnull fields
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String phone = user.getPhoneNumber();
            if (phone == null) {
                // signed in using email
                String name = user.getDisplayName();
                String email = user.getEmail();
                etName.setText(name);
                etEmail.setText(email);
                etEmail.setEnabled(false);
            } else {
                // signed in using phone number
                etPhone.setText(phone);
                etPhone.setEnabled(false);
            }
        }

        final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        final Calendar now = Calendar.getInstance();
        birthday = dateFormat.format(now.getTime());
        dpBirthday.init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        now.set(year, month, day);
                        birthday = dateFormat.format(now.getTime());
                    }
                });

        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get all data
                final String name = etName.getText().toString();
                final String email = etEmail.getText().toString();
                final String phone = etPhone.getText().toString();

                // if any of these is empty, show error
                if (name.trim().length() == 0 ||
                        email.trim().length() == 0 ||
                        phone.trim().length() == 0) {
                    Snackbar.make(view, "Please complete all fields", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // update info to FirebaseUser
                user.updateEmail(email);
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // create user document in users collection
                                    User currentUser = new User(name, email, phone, birthday);
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("users").document(user.getUid()).set(currentUser);

                                    finish();
                                }
                            }
                        });
            }
        });
    }

}
