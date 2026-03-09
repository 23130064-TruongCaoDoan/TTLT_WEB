package dao;

import DTO.MyOrderDTO;
import model.OrderItemsView;
import model.OrderView;

import java.util.List;

public class OrderDao extends BaseDao {

    public int addOrder(int userId, double totalAmount, String note, Integer dis, Integer ship) {
        try {
            return getJdbi().withHandle(handle ->
                    handle.createUpdate("""
                                        INSERT INTO `ORDERS`
                                        (user_id, total_amount, note, status, dis_voucher_id, ship_voucher_id, reviewed)
                                        VALUES (:user_id, :total_amount, :note, :status, :dis_voucher_id, :ship_voucher_id,0)
                                    """)
                            .bind("user_id", userId)
                            .bind("total_amount", totalAmount)
                            .bind("note", note)
                            .bind("status", "Completed")
                            .bind("dis_voucher_id", dis)
                            .bind("ship_voucher_id", ship)
                            .executeAndReturnGeneratedKeys("id")
                            .mapTo(int.class)
                            .one()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<MyOrderDTO> findOrdersByUserId(int userId) {


        String sql = """
                    SELECT 
                        o.id            AS orderId,
                        o.order_date    AS orderDate,
                        o.status        AS status,
                        o.total_amount  AS totalAmount,
                        o.reviewed        AS reviewed,
                
                        SUM(oi.quantity) AS totalQuantity,
                        MIN(b.cover_img_url) AS firstBookImage
                    FROM orders o
                    JOIN order_items oi ON o.id = oi.order_id
                    JOIN books b ON oi.book_id = b.id
                    WHERE o.user_id = :userId
                    GROUP BY o.id, o.order_date, o.status, o.total_amount
                    ORDER BY o.order_date DESC
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(MyOrderDTO.class)
                        .list()
        );
    }


    public List<OrderView> getAllOrder() {

        String sql = """
                    SELECT
                        o.id            AS id,
                        o.user_id       AS userId,
                        u.name     AS userName,
                        d.phone         AS phone,
                        CONCAT_WS(', ',
                                        d.specificAddress,
                                        d.ward,
                                        d.city
                                    ) AS address,
                        o.order_date    AS orderDate,
                        o.status        AS status,
                        o.total_amount  AS totalAmount,
                        o.note          AS note
                    FROM orders o
                    JOIN `user` u ON o.user_id = u.id
                    JOIN shipping s ON o.id = s.order_id
                    JOIN address d ON s.address_id = d.id
                    JOIN order_items oi ON o.id = oi.order_id
                    JOIN books b ON oi.book_id = b.id
                    ORDER BY o.order_date DESC
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(OrderView.class)
                        .list()
        );
    }

    public void setReviewed(int orderId) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("""
                                UPDATE ORDERS SET reviewed = 1 WHERE id = :orderId
                                """)
                        .bind("orderId", orderId)
                        .execute()
        );
    }


    public static void main(String[] args) {
        OrderDao orderDao = new OrderDao();
        System.out.println(orderDao.addOrder(31, 2000, "", 13, 14));
    }

    public boolean updateOrder(int id, Double orderTotalPrice, String orderStatus) {
        return getJdbi().withHandle(handle -> (
                handle.createUpdate("UPDATE `ORDERS` set total_amount = :total, status = :status where id=:id")
                        .bind("total", orderTotalPrice)
                        .bind("status", orderStatus)
                        .bind("id", id)
                        .execute() > 0
        ));
    }

    public boolean deleteOrder(int id) {
        getJdbi().withHandle(handle -> (
                handle.createUpdate("DELETE FROM `SHIPPING` where order_id=:id")
                        .bind("id", id)
                        .execute()
        ));
        getJdbi().withHandle(handle -> (
                handle.createUpdate("DELETE FROM `ORDER_ITEMS` where order_id=:id")
                        .bind("id", id)
                        .execute()
        ));
        return getJdbi().withHandle(handle -> (
                handle.createUpdate("DELETE FROM `ORDERS` where id=:id")
                        .bind("id", id)
                        .execute() > 0
        ));
    }

    public List<OrderView> searchAndFilter(String q, String sortDate) {

        return getJdbi().withHandle(handle -> {

            StringBuilder sql = new StringBuilder("""
                        SELECT
                            o.id            AS id,
                            o.user_id       AS userId,
                            u.name          AS userName,
                            d.phone         AS phone,
                            CONCAT_WS(', ',
                                d.specificAddress,
                                d.ward,
                                d.city
                            ) AS address,
                            o.order_date    AS orderDate,
                            o.status        AS status,
                            o.total_amount  AS totalAmount,
                            o.note          AS note
                        FROM orders o
                        JOIN `user` u ON o.user_id = u.id
                        JOIN shipping s ON o.id = s.order_id
                        JOIN address d ON s.address_id = d.id
                    """);


            if (q != null && !q.isBlank()) {
                sql.append("""
                            WHERE (
                                u.name LIKE :q
                                OR o.id LIKE :q
                                OR o.status LIKE :q
                            )
                        """);
            }

            if ("asc".equals(sortDate)) {
                sql.append(" ORDER BY o.order_date ASC");
            } else {
                sql.append(" ORDER BY o.order_date DESC");
            }

            var query = handle.createQuery(sql.toString());

            if (q != null && !q.isBlank()) {
                query.bind("q", "%" + q + "%");
            }

            return query.mapToBean(OrderView.class).list();
        });
    }


    public List<OrderItemsView> getOrderItemsByOrderId(int orderId) {

        String sql = """
                    SELECT
                        b.id                    AS bookId,
                        b.title                 AS bookName,
                        a.name                  AS author,
                        b.price                 AS price,
                        CAST(oi.quantity AS UNSIGNED) AS quantity,
                        b.type                  AS category,
                        b.age                   AS age,
                        b.cover_img_url         AS image
                    FROM order_items oi
                    JOIN books b ON oi.book_id = b.id
                    LEFT JOIN authors a ON b.author_id = a.id
                    WHERE oi.order_id = :orderId
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderId", orderId)
                        .mapToBean(OrderItemsView.class)
                        .list()
        );
    }

}
