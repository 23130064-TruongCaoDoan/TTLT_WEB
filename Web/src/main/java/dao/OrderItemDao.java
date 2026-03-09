package dao;

import Cart.Cart;
import Cart.CartItem;

public class OrderItemDao extends BaseDao{

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
}
