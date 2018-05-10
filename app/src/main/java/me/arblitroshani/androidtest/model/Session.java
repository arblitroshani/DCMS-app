package me.arblitroshani.androidtest.model;

import java.util.Date;

public class Session {

    private String name;
    private Date date;
    private String doctorId;
    private String description;
    private String diagnosis;

    private int price;

    public Session() {}

    public Session(String name, Date date, String doctorId, String description, String diagnosis, int price) {
        this.name = name;
        this.date = date;
        this.doctorId = doctorId;
        this.description = description;
        this.diagnosis = diagnosis;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
