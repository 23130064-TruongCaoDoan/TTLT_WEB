package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cart {
    private int id;
    private int user_id;

    private HashMap<Integer,CartItem> data = new HashMap<>();

    public Cart() {
    }

    public Cart(int id, int user_id) {
        this.id = id;
        this.user_id = user_id;
        this.data = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public HashMap<Integer, CartItem> getCartItems() {
        return data;
    }

    public void setCartItems(HashMap<Integer, CartItem> cartItems) {
        this.data = cartItems;
    }
    public List<CartItem> getItems() {
        return new ArrayList<>(data.values());
    }

    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : data.values()) {
            total += item.getQuantity();
        }
        return total;
    }
    public double getTotalBill() {
        double total = 0;
        for (CartItem item : data.values()) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public int getQuantityByBookId(int bookId) {
        return data.containsKey(bookId)
                ? data.get(bookId).getQuantity()
                : 0;
    }

}
