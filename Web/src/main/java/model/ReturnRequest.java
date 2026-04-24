package model;

import java.sql.Timestamp;

public class ReturnRequest {
    private int id;
    private int orderId;
    private int userId;
    private String reason;
    private String proofImage;
    private String status;
    private String rejectReason;
    private Timestamp createdAt;

    public ReturnRequest() {
    }

    public ReturnRequest(int id, int orderId, int userId, String reason, String status, String proofImage, String rejectReason, Timestamp createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.reason = reason;
        this.status = status;
        this.proofImage = proofImage;
        this.rejectReason = rejectReason;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }

    public String getProofImage() {
        return proofImage;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setProofImage(String proofImage) {
        this.proofImage = proofImage;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
