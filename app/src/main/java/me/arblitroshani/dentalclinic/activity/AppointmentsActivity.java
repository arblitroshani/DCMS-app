package me.arblitroshani.dentalclinic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.adapter.AppointmentEventRenderer;
import me.arblitroshani.dentalclinic.extra.Constants;
import me.arblitroshani.dentalclinic.extra.Utility;
import me.arblitroshani.dentalclinic.model.FirebaseAppointmentCalendarEvent;

public class AppointmentsActivity extends AppCompatActivity implements CalendarPickerController {

    @BindView(R.id.activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.agenda_calendar_view)
    AgendaCalendarView mAgendaCalendarView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.clAppointments)
    CoordinatorLayout clAppointments;

    private FirebaseFirestore db;

    private Calendar minDate = Calendar.getInstance();
    private Calendar maxDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        minDate.add(Calendar.WEEK_OF_YEAR, -2);
        maxDate.add(Calendar.MONTH, 1);

        initCalendar();

        fab.setOnClickListener(view -> {
            Intent i = new Intent(AppointmentsActivity.this, AppointmentRequestActivity.class);
            startActivityForResult(i, Constants.Appointments.RESULT_OK);
        });
    }

    public void initCalendar() {
        String nationalId = Utility.getNationalIdSharedPreference(this);
        db.collection("appointments")
                .whereEqualTo("nationalId", nationalId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) return;
                    if (snapshot != null) {
                        List<CalendarEvent> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshot) {
                            appointments.add(doc.toObject(FirebaseAppointmentCalendarEvent.class).getCalendarFormat());
                        }
                        mAgendaCalendarView.init(appointments, minDate, maxDate, Locale.getDefault(), this);
                        mAgendaCalendarView.addEventRenderer(new AppointmentEventRenderer());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.Appointments.RESULT_OK) {
            if(resultCode == Activity.RESULT_OK){
                initCalendar();
            }
        }
    }

    @Override
    public void onDaySelected(DayItem dayItem) {}

    @Override
    public void onEventSelected(CalendarEvent event) {
        String eventTitle = event.getTitle();
        if (!eventTitle.equals(getResources().getString(R.string.agenda_event_no_events))) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.dialog_view_event, null);
            final TextView tvDate = alertLayout.findViewById(R.id.tvDateData);
            final TextView tvTime = alertLayout.findViewById(R.id.tvStartTimeData);
            final TextView tvDescriptionData = alertLayout.findViewById(R.id.tvDescriptionData);
            final CheckBox checkbox = alertLayout.findViewById(R.id.chbIsForSelf);

            checkbox.setClickable(false);

            SimpleDateFormat sdfDate = new SimpleDateFormat(Constants.Appointments.DATE_FORMAT, Locale.getDefault());
            SimpleDateFormat sdfTime = new SimpleDateFormat(Constants.Appointments.TIME_FORMAT, Locale.getDefault());

            Date startTime = event.getStartTime().getTime();
            tvDate.setText(sdfDate.format(startTime));
            tvTime.setText(sdfTime.format(startTime));

            AtomicReference<String> docId = new AtomicReference<>();

            // get the extra info from firebase
            db.collection("appointments")
                    .whereEqualTo("startTimeMillis", startTime.getTime())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<FirebaseAppointmentCalendarEvent> firebaseEvents = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                docId.set(doc.getId());
                                firebaseEvents.add(doc.toObject(FirebaseAppointmentCalendarEvent.class));
                            }
                            FirebaseAppointmentCalendarEvent thisEvent = firebaseEvents.get(0);
                            tvDescriptionData.setText(thisEvent.getDescription());
                            checkbox.setEnabled(thisEvent.isForSelf());
                        }
                    });

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(eventTitle);
            alert.setView(alertLayout);
            alert.setCancelable(true);
            alert.setNegativeButton("Delete", (dialog, which) -> {
                db.collection("appointments").document(docId.get())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Snackbar.make(clAppointments, "Appointment cancelled", Snackbar.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Snackbar.make(clAppointments, "Failed to delete", Snackbar.LENGTH_SHORT).show();
                        });
            });
            alert.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
            alert.create().show();
        }
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
    }
}
