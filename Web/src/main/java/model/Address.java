package model;

public class Address {
    private int id;
    private int userId;
    private String name;
    private String phone;
    private String city;

    private String ward;
    private String specificAddress;
    private boolean isDefault;

    public Address(int id, int userId, String name, String phone, String city, String ward, String specificAddress, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.ward = ward;
        this.specificAddress = specificAddress;
        this.isDefault = isDefault;
    }
    public Address() {}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getSpecificAddress() {
        return specificAddress;
    }

    public void setSpecificAddress(String specificAddress) {
        this.specificAddress = specificAddress;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", ward='" + ward + '\'' +
                ", specificAddress='" + specificAddress + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
