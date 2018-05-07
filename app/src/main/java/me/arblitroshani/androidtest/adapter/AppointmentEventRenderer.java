package me.arblitroshani.androidtest.adapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tibolte.agendacalendarview.render.EventRenderer;

import java.text.SimpleDateFormat;
import java.util.Locale;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.extra.Constants;
import me.arblitroshani.androidtest.model.AppointmentCalendarEvent;

public class AppointmentEventRenderer extends EventRenderer<AppointmentCalendarEvent> {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(Constants.Appointments.TIME_FORMAT, Locale.getDefault());

    private static Resources resources;

    @Override
    public void render(View view, AppointmentCalendarEvent event) {

        resources = view.getResources();

        TextView tvTitle = view.findViewById(R.id.view_agenda_event_title);
        TextView tvTime = view.findViewById(R.id.view_agenda_event_time);
        TextView tvStatus = view.findViewById(R.id.view_agenda_event_status);
        LinearLayout container = view.findViewById(R.id.view_agenda_event);

        container.setVisibility(View.VISIBLE);

        tvTitle.setTextColor(resources.getColor(android.R.color.black));
        tvTitle.setText(event.getTitle());

        if (event.getStatus().equals(Constants.Appointments.STATUS_PENDING)) {
            container.setBackgroundColor(resources.getColor(R.color.colorAccent));
            tvStatus.setText("Pending approval");
        } else if (event.getStatus().equals(Constants.Appointments.STATUS_CONFIRMED)) {
            container.setBackgroundColor(Color.parseColor("#028090"));
            tvStatus.setText("Confirmed");
            tvTitle.setTextColor(resources.getColor(R.color.white));
            tvTime.setTextColor(resources.getColor(R.color.gray_very_light));
            tvStatus.setTextColor(resources.getColor(R.color.gray_very_light));
        }

        tvTime.setText(TIME_FORMAT.format(event.getStartTime().getTime()) + " - " + TIME_FORMAT.format(event.getEndTime().getTime()));
    }

    @Override
    public int getEventLayout() {
        return R.layout.view_agenda_appointment_event;
    }

    @Override
    public Class<AppointmentCalendarEvent> getRenderType() {
        return AppointmentCalendarEvent.class;
    }
}
