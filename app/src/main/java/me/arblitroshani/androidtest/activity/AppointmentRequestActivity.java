package me.arblitroshani.androidtest.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.extra.Constants;
import me.arblitroshani.androidtest.model.FirebaseAppointmentCalendarEvent;

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

    private String[] services = {"Orthodontics", "Periodontics"};

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

        ArrayAdapter<String>adapter = new ArrayAdapter<>(AppointmentRequestActivity.this,
                android.R.layout.simple_spinner_item, services);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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
                    updateDateLabel();
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
                    updateTimeLabel();
                },
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void updateDateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        etDatePicker.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateTimeLabel() {
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        etTimePicker.setText(sdf.format(myCalendar.getTime()));
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
                db.collection("appointments")
                        .add(new FirebaseAppointmentCalendarEvent(
                                etDescription.getText().toString(),
                                Constants.Appointments.STATUS_PENDING,
                                spinner.getSelectedItem().toString(),
                                true,
                                myCalendar.getTimeInMillis(),
                                30
                        ))
                        .addOnSuccessListener(documentReference -> {
                            bSubmit.doResult(true);
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result", Constants.Appointments.RESULT_OK);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        })
                        .addOnFailureListener(e -> bSubmit.doResult(false));
                break;
        }
    }
}
