package me.arblitroshani.dentalclinic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Service implements Parcelable {

    private String title;
    private String subtitle;
    private String description;
    private String photoUrl;
    private List<String> photoUrls;

    public Service() {}

    public Service(String title, String subtitle, String description, String photoUrl, List<String> photoUrls) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.photoUrl = photoUrl;
        this.photoUrls = photoUrls;
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    /*    Parcelable methods    */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(subtitle);
        out.writeString(description);
        out.writeString(photoUrl);
        out.writeStringList(photoUrls);
    }

    public Service(Parcel in) {
        title = in.readString();
        subtitle = in.readString();
        description = in.readString();
        photoUrl = in.readString();
        photoUrls = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }
        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };
}
