package me.arblitroshani.androidtest.model;

public class Session {

    private String name;
    private String date;
    private String doctorName;
    private String doctorId;
    private String description;
    private String diagnosis;

    private int price;

    // private List<String> photoLinks;

    public Session() {}

    public Session(String name, String date, String doctorName, String doctorId, String description, String diagnosis, int price) {
        this.name = name;
        this.date = date;
        this.doctorName = doctorName;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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
