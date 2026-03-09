package Cart;

import model.Book;
import model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart implements Serializable {
    Map<Integer, CartItem> data;
    private User user;

    public Cart() {
        this.data = new HashMap<>();
    }

    public void addItem(Book book, int quantity) {
        if (quantity <= 0) quantity = 1;
        if (get(book.getId()) != null) {
            data.get(book.getId()).updateQuantity(quantity);
        } else {
            if (book.getPriceDiscounted() > 0) {
                data.put(book.getId(), new CartItem(book, quantity, book.getPriceDiscounted()));
            } else {
                data.put(book.getId(), new CartItem(book, quantity, book.getPrice()));
            }
        }
    }

    public boolean updateItem(int bookID, int quantity) {
        if (get(bookID) == null) return false;
        if (quantity <= 0) quantity = 1;
        data.get(bookID).setQuantity(quantity);
        return true;
    }

    public CartItem removeItem(int bookID) {
        if (get(bookID) == null) return null;
        return data.remove(bookID);
    }

    public List<CartItem> removeAllItems() {
        List<CartItem> list = new ArrayList<>(data.values());
        data.clear();
        return list;
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

    private CartItem get(int id) {
        return data.get(id);
    }

    public void updateUser(User user) {
        user = new User();
    }

    public String getProductNamesAsString() {
        StringBuilder sb = new StringBuilder();

        for (CartItem item : data.values()) {
            if (item.getBook() != null) {
                sb.append(item.getBook().getTitle()).append(". ");
            }
        }

        // Xóa ". " cuối
        if (sb.length() >= 2) {
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }


    public int getQuantityByBookId(int bookId) {
        return data.containsKey(bookId)
                ? data.get(bookId).getQuantity()
                : 0;
    }
}
