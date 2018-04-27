package me.arblitroshani.androidtest.model;

public class HomeSection {

    private String title;
    private String subtitle;
    private String fragmentOpen;
    private int backgroundColor;
    private int icon;

    public HomeSection(String title, String subtitle, String fragmentOpen, int backgroundColor, int icon) {
        this.title = title;
        this.subtitle = subtitle;
        this.fragmentOpen = fragmentOpen;
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

    public String getFragmentOpen() {
        return fragmentOpen;
    }

    public void setFragmentOpen(String fragmentOpen) {
        this.fragmentOpen = fragmentOpen;
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
