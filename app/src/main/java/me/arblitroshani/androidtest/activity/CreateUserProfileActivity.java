package me.arblitroshani.androidtest.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.model.User;

public class CreateUserProfileActivity extends AppCompatActivity {

    private String birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText etName = findViewById(R.id.etName);
        final EditText etEmail = findViewById(R.id.etEmail);
        final DatePicker dpBirthday = findViewById(R.id.dpBirthday);
        final EditText etPhone = findViewById(R.id.etPhone);

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
