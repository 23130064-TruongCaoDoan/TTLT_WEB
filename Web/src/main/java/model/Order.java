package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private int id;
    private int userId;
    private String orderDate;
    private String status;
    private double totalAmount;
    private String note;
    private String paymentMethod;
    private String paymentStatus;
    private String disVoucherId;
    private String shipVoucherId;

    public Order(int id, int userId, String orderDate, String status, double totalAmount, String note, String disVoucherId, String shipVoucherId) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.note = note;
        this.disVoucherId = disVoucherId;
        this.shipVoucherId = shipVoucherId;
    }
    public Order() {
    }
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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

    public String getOrderDate() {
        DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter output = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        LocalDateTime dt = LocalDateTime.parse(orderDate, input);
        return dt.format(output);
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDisVoucherId() {
        return disVoucherId;
    }

    public void setDisVoucherId(String disVoucherId) {
        this.disVoucherId = disVoucherId;
    }

    public String getShipVoucherId() {
        return shipVoucherId;
    }

    public void setShipVoucherId(String shipVoucherId) {
        this.shipVoucherId = shipVoucherId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    public String getPaymentMethodTransfer() {
        if (paymentMethod == null) return "";

        switch (paymentMethod.toUpperCase()) {
            case "COD":
                return "Thanh toán khi nhận hàng";
            case "VNPAY":
                return "Thanh toán qua VNPay";
            case "MOMO":
                return "Thanh toán qua MOMO";
            default:
                return "Chưa cập nhật";
        }
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderDate='" + orderDate + '\'' +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                ", note='" + note + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", disVoucherId='" + disVoucherId + '\'' +
                ", shipVoucherId='" + shipVoucherId + '\'' +
                '}';
    }
}
