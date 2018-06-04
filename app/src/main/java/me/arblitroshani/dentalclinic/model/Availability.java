package me.arblitroshani.dentalclinic.model;

import java.util.Date;
import java.util.List;

public class Availability {

    private Date date;
    private String note;
    private List<Boolean> availability;

    public Availability() {}

    public Availability(Date date, String note, List<Boolean> availability) {
        this.date = date;
        this.note = note;
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

    public List<Boolean> getAvailability() {
        return availability;
    }

    public void setAvailability(List<Boolean> availability) {
        this.availability = availability;
    }
}
