package me.arblitroshani.androidtest.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.adapter.AppointmentEventRenderer;
import me.arblitroshani.androidtest.extra.Constants;

public class AppointmentsActivity extends AppCompatActivity implements CalendarPickerController {

    @BindView(R.id.activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.agenda_calendar_view)
    AgendaCalendarView mAgendaCalendarView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -1);

        mAgendaCalendarView.init(Constants.Appointments.getMockList(), minDate, maxDate, Locale.getDefault(), this);
        mAgendaCalendarView.addEventRenderer(new AppointmentEventRenderer());

        fab.setOnClickListener(view ->
                Snackbar
                    .make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show());
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
