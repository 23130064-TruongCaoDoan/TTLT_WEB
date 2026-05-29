package model;

import java.sql.Timestamp;

public class HistoryLog {
    private int id;
    private int userId;
    private String actionType;
    private String actionUrl;
    private Timestamp createdAt;

    public HistoryLog() {
    }

    public HistoryLog(int id, int userId, String actionType, String actionUrl, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.actionType = actionType;
        this.actionUrl = actionUrl;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}