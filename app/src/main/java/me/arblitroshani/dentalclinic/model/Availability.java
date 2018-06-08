package me.arblitroshani.dentalclinic.model;

import java.util.Date;
import java.util.List;

public class Availability {

    private Date date;
    private String note;
    private String doctorName;
    private List<Boolean> availability;

    public Availability() {}

    public Availability(Date date, String note, String doctorName, List<Boolean> availability) {
        this.date = date;
        this.note = note;
        this.doctorName = doctorName;
        this.availability = availability;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public List<Boolean> getAvailability() {
        return availability;
    }

    public void setAvailability(List<Boolean> availability) {
        this.availability = availability;
    }
}
