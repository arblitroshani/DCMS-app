package me.arblitroshani.androidtest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.adapter.AppointmentEventRenderer;
import me.arblitroshani.androidtest.adapter.ServicesAdapter;
import me.arblitroshani.androidtest.extra.Constants;
import me.arblitroshani.androidtest.model.AppointmentCalendarEvent;
import me.arblitroshani.androidtest.model.FirebaseAppointmentCalendarEvent;
import me.arblitroshani.androidtest.model.Service;

public class AppointmentsActivity extends AppCompatActivity implements CalendarPickerController {

    @BindView(R.id.activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.agenda_calendar_view)
    AgendaCalendarView mAgendaCalendarView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    Calendar minDate = Calendar.getInstance();
    Calendar maxDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        minDate.add(Calendar.MONTH, -1);

        initCalendar();

        fab.setOnClickListener(view -> {
            Intent i = new Intent(AppointmentsActivity.this, AppointmentRequestActivity.class);
            startActivityForResult(i, 5);
        });
    }

    public void initCalendar() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appointments")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid())
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
        if (requestCode == 5) {
            if(resultCode == Activity.RESULT_OK){
                initCalendar();
            }
        }
    }

    @Override
    public void onDaySelected(DayItem dayItem) {
        Toast.makeText(this, "Day Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        Toast.makeText(this, event.getDayReference().getDate().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
    }
}
