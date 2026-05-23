package model;

public class CartItem {
    private int id;
    private int cart_id;
    private int quantity;
    private double price;
    private Book book;

    public CartItem() {
    }

    public CartItem(int id, int cart_id, int quantity, double price, Book book) {
        this.id = id;
        this.cart_id = cart_id;
        this.quantity = quantity;
        this.price = price;
        this.book = book;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
