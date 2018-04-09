package me.arblitroshani.androidtest.model;

public class User {

    private String name;
    private String email;
    private String phone;
    private String birthday;
    private String type;

    public static final String TYPE_USER = "user";
    public static final String TYPE_DOCTOR = "doctor";
    public static final String TYPE_ADMIN = "admin";

    public User() {}

    public User(String name, String email, String phone, String birthday) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.type = TYPE_USER;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
