package me.arblitroshani.dentalclinic.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.extra.Constants;
import me.arblitroshani.dentalclinic.model.FirebaseAppointmentCalendarEvent;

public class AppointmentRequestActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.sServices)
    Spinner spinner;
    @BindView(R.id.bDone)
    SubmitButton bSubmit;
    @BindView(R.id.etDatePicker)
    EditText etDatePicker;
    @BindView(R.id.etStartTimePicker)
    EditText etTimePicker;
    @BindView(R.id.etDescription)
    EditText etDescription;

    private FirebaseFirestore db;

    private List<String> serviceTitles;
    private List<String> serviceIds;

    private Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_request);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();

        serviceTitles = new ArrayList<>();
        serviceIds = new ArrayList<>();

        // read services from db
        db.collection("servicesHelper").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    serviceIds.add(document.getId());
                    serviceTitles.add(document.getString("title"));
                }
                ArrayAdapter<String>adapter = new ArrayAdapter<>(AppointmentRequestActivity.this,
                        android.R.layout.simple_spinner_item, serviceTitles);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        });

        etDatePicker.setOnClickListener(this);
        etTimePicker.setOnClickListener(this);
        bSubmit.setOnClickListener(this);
    }

    private void pickDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AppointmentRequestActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel(etDatePicker, Constants.Appointments.DATE_FORMAT);
                    pickTime();
                },
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void pickTime() {
        new TimePickerDialog(
                AppointmentRequestActivity.this,
                (timePicker, i, i1) -> {
                    myCalendar.set(Calendar.HOUR_OF_DAY, i);
                    myCalendar.set(Calendar.MINUTE, i1);
                    updateLabel(etTimePicker, Constants.Appointments.TIME_FORMAT);
                },
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void updateLabel(EditText editText, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etDatePicker:
                pickDate();
                break;
            case R.id.etStartTimePicker:
                pickTime();
                break;
            case R.id.bDone:
                etDatePicker.setError(null);
                etTimePicker.setError(null);
                if (TextUtils.isEmpty(etDatePicker.getText())) {
                    etDatePicker.setHint("Date is required!");
                    etDatePicker.setError("Date is required!");
                    bSubmit.reset();
                } else if (TextUtils.isEmpty(etTimePicker.getText())) {
                    etTimePicker.setHint("Please set the time!");
                    etTimePicker.setError("Please set the time!");
                    bSubmit.reset();
                } else {
                    int spinnerIndex = spinner.getSelectedItemPosition();

                    db.collection("appointments")
                            .add(new FirebaseAppointmentCalendarEvent(
                                    this,
                                    etDescription.getText().toString(),
                                    Constants.Appointments.STATUS_PENDING,
                                    serviceTitles.get(spinnerIndex),
                                    serviceIds.get(spinnerIndex),
                                    true,
                                    myCalendar.getTimeInMillis(),
                                    30
                            ))
                            .addOnSuccessListener(documentReference -> {
                                bSubmit.doResult(true);
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result", Constants.Appointments.RESULT_OK);
                                new Handler().postDelayed(() -> {
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }, 600);
                            })
                            .addOnFailureListener(e -> bSubmit.reset());
                }
                break;
        }
    }
}
