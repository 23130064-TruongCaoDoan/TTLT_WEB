package Cart;

import model.Book;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Book book;
    private int quantity;
    private int price;

    public CartItem(Book book, int quantity, int price) {
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public void updateQuantity(int quantity) {
        this.quantity += quantity;
    }
}
