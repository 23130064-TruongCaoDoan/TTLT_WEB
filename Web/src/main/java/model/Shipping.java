package model;

import java.util.Date;

public class Shipping {
    private int orderId;
    private String shippingType;
    private Double shippingCost;
    private Date shippingDate;
    private String deliveredDate;
    private String status;
    private String receiver, ward, districts, city, specificAddress;
    private int phone;


    public Shipping() {
    }


    public Shipping(int orderId, String shippingType, Double shippingCost, Date shippingDate, String deliveredDate, String status, String receiver, String ward, String districts, String city, String specificAddress, int phone) {
        this.orderId = orderId;
        this.shippingType = shippingType;
        this.shippingCost = shippingCost;
        this.shippingDate = shippingDate;
        this.deliveredDate = deliveredDate;
        this.status = status;
        this.receiver = receiver;
        this.ward = ward;
        this.districts = districts;
        this.city = city;
        this.specificAddress = specificAddress;
        this.phone = phone;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistricts() {
        return districts;
    }

    public void setDistricts(String districts) {
        this.districts = districts;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSpecificAddress() {
        return specificAddress;
    }

    public void setSpecificAddress(String specificAddress) {
        this.specificAddress = specificAddress;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Shipping{" +
                "orderId=" + orderId +
                ", shippingType='" + shippingType + '\'' +
                ", shippingCost=" + shippingCost +
                ", shippingDate=" + shippingDate +
                ", deliveredDate='" + deliveredDate + '\'' +
                ", status='" + status + '\'' +
                ", receiver='" + receiver + '\'' +
                ", ward='" + ward + '\'' +
                ", districts='" + districts + '\'' +
                ", city='" + city + '\'' +
                ", specificAddress='" + specificAddress + '\'' +
                ", phone=" + phone +
                '}';
    }
}
