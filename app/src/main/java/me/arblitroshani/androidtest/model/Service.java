package me.arblitroshani.androidtest.model;

public class Service {

    private String title;
    private String photoUrl;

    public Service() {}

    public Service(String title, String photoUrl) {
        this.title = title;
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
