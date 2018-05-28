package me.arblitroshani.dentalclinic.model;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Exclude;

import java.util.Calendar;

import me.arblitroshani.dentalclinic.extra.Utility;

public class FirebaseAppointmentCalendarEvent {

    private String patientName;
    private String nationalId;

    private String title;
    private String description;
    private String status;
    private String service;
    private boolean isForSelf;

    private long startTimeMillis;
    private int durationMinutes;

    public FirebaseAppointmentCalendarEvent() {}

    public FirebaseAppointmentCalendarEvent(Context context,  String description, String status,
                                            String service, boolean isForSelf, long startTimeMillis,
                                            int durationMinutes) {
        this.title = service + " Appointment";
        this.description = description;
        this.status = status;
        this.service = service;
        this.isForSelf = isForSelf;
        this.startTimeMillis = startTimeMillis;
        this.durationMinutes = durationMinutes;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.patientName = currentUser.getDisplayName();
        this.nationalId = Utility.getNationalIdSharedPreference(context);
    }

    @Exclude
    public AppointmentCalendarEvent getCalendarFormat() {
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        startTime.setTimeInMillis(this.startTimeMillis);
        endTime.setTimeInMillis(this.startTimeMillis + this.durationMinutes * 60 * 1000);

        return new AppointmentCalendarEvent(
                this.getTitle(),
                this.getDescription(),
                startTime,
                endTime,
                this.getStatus(),
                this.getService(),
                this.isForSelf
        );
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
