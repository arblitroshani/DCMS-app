package me.arblitroshani.dentalclinic.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.Exclude;

import me.arblitroshani.dentalclinic.extra.Config;

public class User implements Parcelable {

    public static final String TYPE_USER = "user";
    public static final String TYPE_DOCTOR = "doctor";
    public static final String TYPE_ADMIN = "admin";

    private String name;
    private String surname;
    private String email;
    private String phone;
    private String birthday;
    private String sex;
    private String documentNumber;
    private String uid;
    private String nationality;
    private String type;

    public User() {}

    public User(String name, String surname, String email, String phone, String birthday, String sex, String documentNumber, String uid, String nationality, String type) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.sex = sex;
        this.documentNumber = documentNumber;
        this.uid = uid;
        this.nationality = nationality;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Exclude
    public String getFullName() {
        return name + " " + surname;
    }

    public static String getHighResGmailPhotoUrl(Uri url) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        for (UserInfo user : auth.getCurrentUser().getProviderData()) {
            if (user.getProviderId().equals("google.com") && url != null) {
                final String originalPieceOfUrl = "s96-c/photo.jpg";
                final String newPieceOfUrlToAdd = "s400-c/photo.jpg";
                String photoPath = url.toString();
                return photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
            }
        }
        return Config.DEFAULT_PROFILE_PICTURE;
    }

    public User(Parcel in) {
        name = in.readString();
        surname = in.readString();
        email = in.readString();
        phone = in.readString();
        birthday = in.readString();
        sex = in.readString();
        documentNumber = in.readString();
        uid = in.readString();
        nationality = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(name);
        out.writeString(surname);
        out.writeString(email);
        out.writeString(phone);
        out.writeString(birthday);
        out.writeString(sex);
        out.writeString(documentNumber);
        out.writeString(uid);
        out.writeString(nationality);
        out.writeString(type);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
