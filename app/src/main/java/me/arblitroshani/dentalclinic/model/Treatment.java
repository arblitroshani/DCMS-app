package me.arblitroshani.dentalclinic.model;

import java.util.Date;

public class Treatment {

    private String service;
    private String description;
    private String doctorId;
    private String nationalId;
    private Date dateStarted;
    private boolean isOngoing;
    private int numSessions;

    public Treatment() {}

    public Treatment(String service, String description, String doctorId, String nationalId, Date dateStarted, boolean isOngoing, int numSessions) {
        this.service = service;
        this.description = description;
        this.doctorId = doctorId;
        this.nationalId = nationalId;
        this.dateStarted = dateStarted;
        this.isOngoing = isOngoing;
        this.numSessions = numSessions;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public boolean isOngoing() {
        return isOngoing;
    }

    public void setOngoing(boolean ongoing) {
        isOngoing = ongoing;
    }

    public int getNumSessions() {
        return numSessions;
    }

    public void setNumSessions(int numSessions) {
        this.numSessions = numSessions;
    }
}
