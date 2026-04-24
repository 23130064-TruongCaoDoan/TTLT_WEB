package DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MyOrderDTO {
    private int orderId;
    private String orderDate;
    private String status;
    private double totalAmount;
    private int totalQuantity;
    private String firstBookImage;



    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getFirstBookImage() {
        return firstBookImage;
    }

    public void setFirstBookImage(String firstBookImage) {
        this.firstBookImage = firstBookImage;
    }


    @Override
    public String toString() {
        return "MyOrderDTO{" +
                "orderId=" + orderId +
                ", orderDate='" + orderDate + '\'' +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                ", totalQuantity=" + totalQuantity +
                ", firstBookImage='" + firstBookImage + '\'' +
                '}';
    }

    public boolean isCanReturn() {
        try {
            if (this.status == null || !this.status.equalsIgnoreCase("COMPLETED")) {
                return false;
            }

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime orderDateTime = LocalDateTime.parse(this.orderDate, inputFormatter);

            long daysBetween = ChronoUnit.DAYS.between(orderDateTime, LocalDateTime.now());

            return daysBetween <= 7;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
