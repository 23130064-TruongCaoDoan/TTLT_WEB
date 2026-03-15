package model;

import java.time.LocalDateTime;

public class Notification {

    private int notiId;
    private int userId;
    private String title;
    private String noti;
    private String createdAt;
    private boolean read;

    public Notification() {
    }

    public Notification(int userId, String title, String noti, String createdAt) {
        this.userId = userId;
        this.title = title;
        this.noti = noti;
        this.createdAt = createdAt;
    }

    public int getNotiId() {
        return notiId;
    }

    public void setNotiId(int notiId) {
        this.notiId = notiId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoti() {
        return noti;
    }

    public void setNoti(String noti) {
        this.noti = noti;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}

