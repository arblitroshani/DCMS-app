package me.arblitroshani.dentalclinic.model;

import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import java.util.Calendar;
import me.arblitroshani.dentalclinic.R;

public class AppointmentCalendarEvent extends BaseCalendarEvent {

    private String patientName;
    private String status;
    private String service;
    private boolean isForSelf;

    public AppointmentCalendarEvent(String patientName, String title, String description, Calendar startTime,
                                    Calendar endTime, String status, String service,
                                    boolean isForSelf) {
        super(title, description, "", R.color.gray_light, startTime, endTime, true);
        this.patientName = patientName;
        this.status = status;
        this.service = service;
        this.isForSelf = isForSelf;
    }

    public AppointmentCalendarEvent(AppointmentCalendarEvent calendarEvent) {
        super(calendarEvent.getTitle(), calendarEvent.getDescription(), calendarEvent.getLocation(),
                calendarEvent.getColor(), calendarEvent.getStartTime(), calendarEvent.getEndTime(),
                calendarEvent.isAllDay());
        this.patientName = calendarEvent.getPatientName();
        this.status = calendarEvent.getStatus();
        this.service = calendarEvent.getService();
        this.isForSelf = calendarEvent.isForSelf();
    }

    @Override
    public CalendarEvent copy() {
        return new AppointmentCalendarEvent(this);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean isForSelf() {
        return isForSelf;
    }

    public void setForSelf(boolean forSelf) {
        isForSelf = forSelf;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
