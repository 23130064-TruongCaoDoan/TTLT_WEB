package dao;

import Cart.Cart;
import Cart.CartItem;
import model.Book;

import java.util.Map;

public class OrderItemDao extends BaseDao {

    BookDao bookDao = new BookDao();

    public void insertOrderItems(int orderId, Cart cart) {
        if (cart == null || cart.getItems().isEmpty()) return;

        getJdbi().useHandle(handle -> {
            var batch = handle.prepareBatch("""
                        INSERT INTO order_items
                        (order_id, book_id, quantity, subtotal)
                        VALUES (:order_id, :book_id, :quantity, :subtotal)
                    """);

            for (CartItem item : cart.getItems()) {
                batch
                        .bind("order_id", orderId)
                        .bind("book_id", item.getBook().getId())
                        .bind("quantity", item.getQuantity())
                        .bind("subtotal", item.getPrice() * item.getQuantity())
                        .add();
            }
            batch.execute();
        });
    }

    public void insertOrderItems(int orderId, Map<Integer, Integer> productMap) {
        if (productMap == null) return;

        getJdbi().useHandle(handle -> {
            var batch = handle.prepareBatch("""
                        INSERT INTO order_items
                        (order_id, book_id, quantity, subtotal)
                        VALUES (:order_id, :book_id, :quantity, :subtotal)
                    """);
            for (Map.Entry<Integer, Integer> entry : productMap.entrySet()) {
                int book_id = entry.getKey();
                int quantity = entry.getValue();
                Book book = bookDao.getBookById(book_id);
                batch
                        .bind("order_id", orderId)
                        .bind("book_id", book_id)
                        .bind("quantity", quantity);
                if (book.getPriceDiscounted() > 0) {
                    batch.bind("subtotal", book.getPriceDiscounted() * quantity);
                } else {
                    batch.bind("subtotal", book.getPrice() * quantity);
                }
                batch.add();
            }
            batch.execute();

        });
    }
}
