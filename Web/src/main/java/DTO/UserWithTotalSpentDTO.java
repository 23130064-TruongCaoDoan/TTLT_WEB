package DTO;

public class UserWithTotalSpentDTO {
    private int id;
    private String name;
    private String email;
    private int point;
    private double totalSpent;
    private int role;
    private int status;
    public int getId() {
        return id;
    }
    public String getCustomerCode() {
        return String.format("KH%02d", id);
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    @Override
    public String toString() {
        return "UserWithTotalSpentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", point=" + point +
                ", totalSpent=" + totalSpent +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}
