package model;

import java.time.LocalDate;
import java.util.Date;

public class User {
    private int id;
    private String name;

    private String password_hash;

    private boolean role;

    private boolean sex;

    private String phone;

    private LocalDate birthday;

    private int point;

    private boolean status;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public boolean isRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return name;
    }

    public String getDisplayName() {
        if (name == null) return "";

        String trimmed = name.trim();
        if (trimmed.isEmpty()) return "";

        String[] parts = trimmed.split("\\s+");

        if (parts.length == 1) {
            return parts[0];
        }
        String last = parts[parts.length - 1];
        String secondLast = parts[parts.length - 2];
        String twoLast = secondLast + " " + last;

        if (twoLast.length() < 15) {
            return twoLast;
        }
        return last;
    }

}
