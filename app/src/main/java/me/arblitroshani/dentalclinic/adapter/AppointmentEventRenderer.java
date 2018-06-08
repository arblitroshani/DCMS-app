package me.arblitroshani.dentalclinic.adapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tibolte.agendacalendarview.render.EventRenderer;

import java.text.SimpleDateFormat;
import java.util.Locale;

import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.extra.Constants;
import me.arblitroshani.dentalclinic.model.AppointmentCalendarEvent;

public class AppointmentEventRenderer extends EventRenderer<AppointmentCalendarEvent> {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(Constants.Appointments.TIME_FORMAT, Locale.getDefault());

    private static Resources resources;

    private boolean isDoctor;

    public AppointmentEventRenderer(boolean isDoctor) {
        this.isDoctor = isDoctor;
    }

    @Override
    public void render(View view, AppointmentCalendarEvent event) {

        resources = view.getResources();

        TextView tvTitle = view.findViewById(R.id.view_agenda_event_title);
        TextView tvTime = view.findViewById(R.id.view_agenda_event_time);
        TextView tvStatus = view.findViewById(R.id.view_agenda_event_status);
        LinearLayout container = view.findViewById(R.id.view_agenda_event);

        container.setVisibility(View.VISIBLE);

        tvTitle.setTextColor(resources.getColor(android.R.color.black));

        if (isDoctor) {
            tvTitle.setText(event.getPatientName());
        } else {
            tvTitle.setText(event.getTitle());
        }

        if (event.getStatus().equals(Constants.Appointments.STATUS_PENDING)) {
            container.setBackgroundColor(resources.getColor(R.color.colorAccent));
            tvStatus.setText("Pending approval");
        } else if (event.getStatus().equals(Constants.Appointments.STATUS_CONFIRMED)) {
            container.setBackgroundColor(resources.getColor(R.color.appointmentConfirmed));
            tvStatus.setText("Confirmed");
            tvTitle.setTextColor(resources.getColor(R.color.white));
            tvTime.setTextColor(resources.getColor(R.color.gray_very_light));
            tvStatus.setTextColor(resources.getColor(R.color.gray_very_light));
        }

        tvTime.setText(TIME_FORMAT.format(event.getStartTime().getTime()) + " - " + TIME_FORMAT.format(event.getEndTime().getTime()));
    }

    @Override
    public int getEventLayout() {
        if (isDoctor)
            return R.layout.item_doctor_appointment_event;
        return R.layout.item_appointment_event;
    }

    @Override
    public Class<AppointmentCalendarEvent> getRenderType() {
        return AppointmentCalendarEvent.class;
    }
}
