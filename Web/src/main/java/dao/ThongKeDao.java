package dao;

import DTO.BookWithSoldDTO;
import DTO.RevenueDTO;
import DTO.UserWithTotalSpentDTO;

import java.util.List;
import java.util.Optional;

public class ThongKeDao extends BaseDao {

    public List<UserWithTotalSpentDTO> getTop10UsersWithTotalSpent() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                SELECT 
                    u.id,
                    u.name,
                    u.email,
                    u.point,
                    SUM(o.total_amount) AS totalSpent
                FROM `user` u
                JOIN orders o ON o.user_id = u.id
                WHERE u.role = 0
                  AND o.status = 'COMPLETED'
                GROUP BY u.id, u.name, u.email, u.point
                ORDER BY totalSpent DESC
                LIMIT 10
            """)
                        .mapToBean(UserWithTotalSpentDTO.class)
                        .list()
        );
    }

    public double getTotalRevenue() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                SELECT COALESCE(SUM(o.total_amount), 0)
                FROM orders o
                JOIN `user` u ON o.user_id = u.id
                WHERE u.role = 0
                  AND o.status = 'COMPLETED'
            """)
                        .mapTo(double.class)
                        .one()
        );
    }
    public Optional<UserWithTotalSpentDTO> getTopCustomer() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
            SELECT 
                u.id,
                u.name,
                u.email,
                u.point,
                SUM(o.total_amount) AS totalSpent
            FROM `user` u
            JOIN orders o ON o.user_id = u.id
            WHERE u.role = 0
              AND o.status = 'COMPLETED'
            GROUP BY u.id, u.name, u.email, u.point
            ORDER BY totalSpent DESC
            LIMIT 1
        """)
                        .mapToBean(UserWithTotalSpentDTO.class)
                        .findOne()
        );
    }
    public Optional<BookWithSoldDTO> getBestSeller() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
            SELECT 
                b.id,
                b.book_code AS bookCode,
                b.title,
                b.price,
                b.type,
                b.age,
                b.cover_img_url AS coverImgUrl,
                SUM(oi.quantity) AS totalSold
            FROM books b
            JOIN order_items oi ON oi.book_id = b.id
            JOIN orders o ON o.id = oi.order_id
            WHERE o.status = 'COMPLETED'
            GROUP BY b.id
            ORDER BY totalSold DESC
            LIMIT 1
        """)
                        .mapToBean(BookWithSoldDTO.class)
                        .findOne()
        );
    }
    public Optional<BookWithSoldDTO> getWorstSeller() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
            SELECT 
                b.id,
                b.book_code AS bookCode,
                b.title,
                b.price,
                b.type,
                b.age,
                b.cover_img_url AS coverImgUrl,
                SUM(oi.quantity) AS totalSold
            FROM books b
            JOIN order_items oi ON oi.book_id = b.id
            JOIN orders o ON o.id = oi.order_id
            WHERE o.status = 'COMPLETED'
            GROUP BY b.id
            ORDER BY totalSold ASC
            LIMIT 1
        """)
                        .mapToBean(BookWithSoldDTO.class)
                        .findOne()
        );
    }
    public List<BookWithSoldDTO> getTop10Books() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
            SELECT 
                b.id,
                b.book_code AS bookCode,
                b.title,
                b.price,
                b.type,
                b.age,
                b.cover_img_url AS coverImgUrl,
                SUM(oi.quantity) AS totalSold
            FROM books b
            JOIN order_items oi ON oi.book_id = b.id
            JOIN orders o ON o.id = oi.order_id
            WHERE o.status = 'COMPLETED'
            GROUP BY b.id
            ORDER BY totalSold DESC
            LIMIT 10
        """)
                        .mapToBean(BookWithSoldDTO.class)
                        .list()
        );
    }
    public List<RevenueDTO> getRevenue(String type) {
        String sql;

        switch (type) {
            case "day":
                sql = """
                SELECT DATE(order_date) AS label,
                       SUM(total_amount) AS revenue
                FROM orders
                WHERE status = 'COMPLETED'
                GROUP BY DATE(order_date)
                ORDER BY DATE(order_date)
            """;
                break;

            case "week":
                sql = """
                SELECT CONCAT('Tuần ', WEEK(order_date,1)) AS label,
                       SUM(total_amount) AS revenue
                FROM orders
                WHERE status = 'COMPLETED'
                GROUP BY WEEK(order_date,1), CONCAT('Tuần ', WEEK(order_date,1))
                ORDER BY WEEK(order_date,1)
            """;
                break;

            case "year":
                sql = """
                SELECT YEAR(order_date) AS label,
                       SUM(total_amount) AS revenue
                FROM orders
                WHERE status = 'COMPLETED'
                GROUP BY YEAR(order_date)
                ORDER BY YEAR(order_date)
            """;
                break;

            default: // month
                sql = """
                SELECT CONCAT('Tháng ', MONTH(order_date)) AS label,
                       SUM(total_amount) AS revenue
                FROM orders
                WHERE status = 'COMPLETED'
                GROUP BY MONTH(order_date), CONCAT('Tháng ', MONTH(order_date))
                ORDER BY MONTH(order_date)
            """;
        }

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .mapToBean(RevenueDTO.class)
                        .list()
        );
    }



}
