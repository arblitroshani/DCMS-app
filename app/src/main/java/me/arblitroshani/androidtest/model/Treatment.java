package me.arblitroshani.androidtest.model;

public class Treatment {

    private String name;
    private String categoryName;
    private String dateStarted;

    private boolean consentIsAgreed;
    private boolean statusIsFinished;

    private int paymentTotal;
    private int numSessions;

    public Treatment() {}

    public Treatment(String name, String categoryName, String dateStarted, boolean consentIsAgreed, boolean statusIsFinished, int paymentTotal) {
        this.name = name;
        this.categoryName = categoryName;
        this.dateStarted = dateStarted;
        this.consentIsAgreed = consentIsAgreed;
        this.statusIsFinished = statusIsFinished;
        this.paymentTotal = paymentTotal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public boolean isConsentIsAgreed() {
        return consentIsAgreed;
    }

    public void setConsentIsAgreed(boolean consentIsAgreed) {
        this.consentIsAgreed = consentIsAgreed;
    }

    public boolean isStatusIsFinished() {
        return statusIsFinished;
    }

    public void setStatusIsFinished(boolean statusIsFinished) {
        this.statusIsFinished = statusIsFinished;
    }

    public int getPaymentTotal() {
        return paymentTotal;
    }

    public void setPaymentTotal(int paymentTotal) {
        this.paymentTotal = paymentTotal;
    }
}
