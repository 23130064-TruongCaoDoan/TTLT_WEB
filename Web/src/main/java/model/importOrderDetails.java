package model;

public class importOrderDetails {
    private int id;
    private int importOrderId;
    private int quantity;
    private int pricePort;
    private int subtotal;

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getPricePort() {
        return pricePort;
    }

    public void setPricePort(int pricePort) {
        this.pricePort = pricePort;
    }

    public int getImportOrderId() {
        return importOrderId;
    }

    public void setImportOrderId(int importOrderId) {
        this.importOrderId = importOrderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
