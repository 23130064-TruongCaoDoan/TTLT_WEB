package model;

import java.util.Date;

public class Shipping {
    private int orderId;
    private int addressId;
    private String shippingType;
    private Double shippingCost;
    private Date shippingDate;
    private String deliveredDate;
    private String status;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
