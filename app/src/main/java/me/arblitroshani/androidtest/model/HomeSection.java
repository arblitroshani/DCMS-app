package me.arblitroshani.androidtest.model;

import android.graphics.Color;

public class HomeSection {

    private String title;
    private String subtitle;
    private int backgroundColor;
    private int icon;

    public HomeSection(String title, String subtitle, int backgroundColor, int icon) {
        this.title = title;
        this.subtitle = subtitle;
        this.backgroundColor = backgroundColor;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
