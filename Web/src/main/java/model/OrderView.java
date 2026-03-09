package model;

public class OrderView {
    private int id;
    private int userId;
    private String userName;
    private String phone;
    private String address;
    private String orderDate;
    private String status;
    private int totalAmount;
    private String note;

    public OrderView() {
    }

    public OrderView(int id, int userId, String userName,
                     String phone, String address,
                     String orderDate, String status,
                     int totalAmount, String note) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.phone = phone;
        this.address = address;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.note = note;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderDate() {
        return orderDate;
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

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

