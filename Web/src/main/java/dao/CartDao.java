package dao;

import model.Book;
import model.Cart;
import model.CartItem;

import java.util.HashMap;
import java.util.List;

public class CartDao extends BaseDao {


    public void addToCart(int cartId, int bookId, int quantity) {

        getJdbi().useHandle(handle -> {

            Integer existingId = handle.createQuery("""
                SELECT id FROM cart_items
                WHERE cart_id = :cartId AND book_id = :bookId
                """)
                    .bind("cartId", cartId)
                    .bind("bookId", bookId)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);

            if (existingId != null) {
                handle.createUpdate("""
                    UPDATE cart_items
                    SET quantity = quantity + :quantity
                    WHERE id = :id
                    """)
                        .bind("quantity", quantity)
                        .bind("id", existingId)
                        .execute();

            } else {

                BookDao bookDao = new BookDao();
                Book book = bookDao.getBookById(bookId);

                int price = (book.getPriceDiscounted() > 0)
                        ? book.getPriceDiscounted()
                        : book.getPrice();

                handle.createUpdate("""
                    INSERT INTO cart_items(cart_id, quantity, price, book_id)
                    VALUES (:cartId, :quantity, :price, :bookId)
                    """)
                        .bind("cartId", cartId)
                        .bind("quantity", quantity)
                        .bind("price", price)
                        .bind("bookId", bookId)
                        .execute();
            }
        });
    }

    public Cart getCart(int userId) {
        return getJdbi().withHandle(handle -> {

            Cart cart = handle.createQuery("""
                            SELECT id, user_id
                            FROM cart
                            WHERE user_id = :userId
                            """)
                    .bind("userId", userId)
                    .mapToBean(Cart.class)
                    .findOne()
                    .orElse(null);

            if (cart == null) return null;

            List<CartItem> items = getCartItemsByCartId(cart.getId());

            HashMap<Integer, CartItem> map = new HashMap<>();
            for (CartItem item : items) {
                map.put(item.getBook().getId(), item);
            }

            cart.setCartItems(map);

            return cart;
        });
    }

    public List<CartItem> getCartItemsByCartId(int cartId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                SELECT 
                    ci.id,
                    ci.cart_id,
                    ci.quantity,
                    ci.price,
                    b.id as book_id,
                    b.title,
                    b.price as book_price,
                    b.price_discounted as book_price_discounted,
                    b.book_code,
                    b.author_id,
                    b.type,
                    b.age,
                    b.cover_img_url,
                    b.description,
                    b.publisher,
                    b.provider,
                    b.published_date,
                    b.weight,
                    b.book_size,
                    b.pages_number,
                    b.format,
                    b.is_sell,
                    b.add_date,
                    b.quantity_sold,
                    b.stock
                FROM cart_items ci
                JOIN books b ON ci.book_id = b.id
                WHERE ci.cart_id = :cartId
                """)
                        .bind("cartId", cartId)
                        .map((rs, ctx) -> {
                            Book book = new Book();
                            book.setId(rs.getInt("book_id"));
                            book.setTitle(rs.getString("title"));
                            book.setBookCode(rs.getString("book_code"));
                            book.setAuthorId(rs.getInt("author_id"));
                            book.setType(rs.getString("type"));
                            book.setAge(rs.getInt("age"));
                            book.setCoverImgUrl(rs.getString("cover_img_url"));
                            book.setDescription(rs.getString("description"));
                            book.setPublisher(rs.getString("publisher"));
                            book.setProvider(rs.getString("provider"));
                            book.setPublishedDate(rs.getInt("published_date"));
                            book.setWeight(rs.getDouble("weight"));
                            book.setBookSize(rs.getString("book_size"));
                            book.setPagesNumber(rs.getInt("pages_number"));
                            book.setFormat(rs.getString("format"));
                            book.setIsSell(rs.getBoolean("is_sell"));
                            book.setAdd_date(rs.getString("add_date"));
                            book.setQuantitySold(rs.getInt("quantity_sold"));
                            book.setStock(rs.getInt("stock"));

                            int price = rs.getInt("book_price");
                            int priceDiscounted = rs.getInt("book_price_discounted");
                            book.setPrice(price);
                            book.setPriceDiscounted(priceDiscounted);

                            return new CartItem(
                                    rs.getInt("id"),
                                    rs.getInt("cart_id"),
                                    rs.getInt("quantity"),
                                    rs.getDouble("price"),
                                    book
                            );
                        })
                        .list()
        );
    }

    public void createCart(int id) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("INSERT INTO CART(user_id) VALUES (:id)")
                        .bind("id", id)
                        .execute()
        );
    }

    public void updateItem(int cartId, int id1, int quantity) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("UPDATE CART_ITEMS SET quantity = :quantity WHERE book_id = :id AND cart_id = :cartId")
                        .bind("quantity", quantity)
                        .bind("cartId", cartId)
                        .bind("id",id1)
                        .execute()
                );
    }

    public void removeItem(int cartId, int id1) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("DELETE FROM CART_ITEMS WHERE book_id = :id AND cart_id = :cartId")
                        .bind("id", id1)
                        .bind("cartId", cartId)
                        .execute()
                );
    }

    public void removeAllItems(int cartId) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("DELETE FROM CART_ITEMS WHERE cart_id = :cartId")
                        .bind("cartId", cartId)
                        .execute()
        );
    }
}
