package model;

public class Publisher {
    private int id;
    private String publisherCode;
    private String name;
    private String address;
    private String email;
    private String phone;

    public Publisher() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPublisherCode() { return publisherCode; }
    public void setPublisherCode(String publisherCode) { this.publisherCode = publisherCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}