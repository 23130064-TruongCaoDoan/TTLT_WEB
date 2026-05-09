package dao;

import DTO.BookWithSoldDTO;
import DTO.OrderDTOChart;
import DTO.RevenueDTO;
import DTO.UserWithTotalSpentDTO;
import model.Book;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ThongKeDao extends BaseDao {

    public List<UserWithTotalSpentDTO> getTop10UsersWithTotalSpent(LocalDate from, LocalDate to) {
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
                  AND o.status = 'COMPLETED' AND o.order_date BETWEEN :from AND :to
                GROUP BY u.id, u.name, u.email, u.point
                ORDER BY totalSpent DESC
                LIMIT 10
            """)
                        .bind("from",from)
                        .bind("to",to)
                        .mapToBean(UserWithTotalSpentDTO.class)
                        .list()
        );
    }
    public List<UserWithTotalSpentDTO> getTop10UsersWithTotalSpent(String year) {
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
                  AND o.status = 'COMPLETED' AND YEAR(o.order_date) = :year
                GROUP BY u.id, u.name, u.email, u.point
                ORDER BY totalSpent DESC
                LIMIT 10
            """)
                        .bind("year",year)
                        .mapToBean(UserWithTotalSpentDTO.class)
                        .list()
        );
    }

    public double getTotalRevenue(LocalDate from, LocalDate to) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                SELECT COALESCE(SUM(total_amount), 0)
                FROM ORDERS o
                JOIN USER u ON o.user_id = u.id
                WHERE u.role = 0 AND o.status = 'COMPLETED' AND o.order_date BETWEEN :from AND :to
            """)
                        .bind("from", from)
                        .bind("to", to.plusDays(1))
                        .mapTo(double.class)
                        .one()
        );
    }

    public double getTotalRevenue(String year) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                SELECT COALESCE(SUM(total_amount), 0)
                FROM ORDERS o
                JOIN USER u ON o.user_id = u.id
                WHERE u.role = 0 AND o.status = 'COMPLETED' AND YEAR(o.order_date) = :year
            """)
                        .bind("year", year)
                        .mapTo(double.class)
                        .one()
        );
    }

    public Optional<UserWithTotalSpentDTO> getTopCustomer(LocalDate from, LocalDate to) {
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
              AND o.order_date BETWEEN :from AND :to
            GROUP BY u.id, u.name, u.email, u.point
            ORDER BY totalSpent DESC
            LIMIT 1
        """)
                        .bind("from", from)
                        .bind("to", to.plusDays(1))
                        .mapToBean(UserWithTotalSpentDTO.class)
                        .findOne()
        );
    }
    public Optional<UserWithTotalSpentDTO> getTopCustomer(String year) {
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
              AND YEAR(o.order_date) =  :year
            GROUP BY u.id, u.name, u.email, u.point
            ORDER BY totalSpent DESC
            LIMIT 1
        """)
                        .bind("year",year)
                        .mapToBean(UserWithTotalSpentDTO.class)
                        .findOne()
        );
    }
    public Optional<BookWithSoldDTO> getBestSeller(LocalDate from, LocalDate to) {
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
            JOIN user u ON o.user_id = u.id
            WHERE u.role = 0 AND o.status = 'COMPLETED' AND o.order_date BETWEEN :from AND :to
            GROUP BY b.id
            ORDER BY totalSold DESC
            LIMIT 1
        """)
                        .bind("from", from)
                        .bind("to", to.plusDays(1))
                        .mapToBean(BookWithSoldDTO.class)
                        .findOne()
        );
    }
    public Optional<BookWithSoldDTO> getBestSeller(String year) {
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
            JOIN user u ON o.user_id = u.id
            WHERE u.role = 0 AND o.status = 'COMPLETED' AND YEAR(o.order_date) = :year
            GROUP BY b.id
            ORDER BY totalSold DESC
            LIMIT 1
        """)
                        .bind("year", year)
                        .mapToBean(BookWithSoldDTO.class)
                        .findOne()
        );
    }
    public Optional<BookWithSoldDTO> getWorstSeller(LocalDate from, LocalDate to) {
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
            JOIN user u ON o.user_id = u.id
            WHERE u.role = 0 AND o.status = 'COMPLETED' AND o.order_date BETWEEN :from AND :to
            GROUP BY b.id
            ORDER BY totalSold ASC
            LIMIT 1
        """)
                        .bind("from", from)
                        .bind("to", to.plusDays(1))
                        .mapToBean(BookWithSoldDTO.class)
                        .findOne()
        );
    }
    public Optional<BookWithSoldDTO> getWorstSeller(String year) {
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
            JOIN user u ON o.user_id = u.id
            WHERE u.role = 0 AND o.status = 'COMPLETED' AND YEAR(o.order_date) = :year
            GROUP BY b.id
            ORDER BY totalSold ASC
            LIMIT 1
        """)
                        .bind("year", year)
                        .mapToBean(BookWithSoldDTO.class)
                        .findOne()
        );
    }
    public List<BookWithSoldDTO> getTop10Books(LocalDate from, LocalDate to) {
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
            JOIN user u ON o.user_id = u.id
            WHERE u.role = 0 AND o.status = 'COMPLETED' AND o.order_date BETWEEN :from AND :to
            GROUP BY b.id
            ORDER BY totalSold DESC
            LIMIT 10
        """)
                        .bind("from", from)
                        .bind("to", to)
                        .mapToBean(BookWithSoldDTO.class)
                        .list()
        );
    }
    public List<BookWithSoldDTO> getTop10Books(String year) {
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
            JOIN user u ON o.user_id = u.id
            WHERE u.role = 0 AND o.status = 'COMPLETED' AND YEAR(o.order_date) = :year
            GROUP BY b.id
            ORDER BY totalSold DESC
            LIMIT 10
        """)
                        .bind("year", year)
                        .mapToBean(BookWithSoldDTO.class)
                        .list()
        );
    }
    public List<RevenueDTO> getRevenueChart(LocalDate from, LocalDate to) {
        return  getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT DATE(o.order_date) AS label, SUM(o.total_amount) AS revenue
                        FROM ORDERS o
                        INNER JOIN USER u ON u.id = o.user_id
                        WHERE u.role = 0 AND o.status = 'COMPLETED' AND o.order_date BETWEEN :from AND :to
                        GROUP BY DATE(o.order_date)
                        ORDER BY DATE(o.order_date)
                        LIMIT 30
                        """)
                        .bind("from",from)
                        .bind("to", to)
                        .mapToBean(RevenueDTO.class)
                        .list()
        );
    }
    public List<RevenueDTO> getRevenueChart(String year) {
        return  getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT CONCAT('Tháng ', MONTH(o.order_date)) AS label, SUM(total_amount) AS revenue
                        FROM ORDERS o
                        INNER JOIN USER u ON u.id = o.user_id
                        WHERE u.role = 0 AND o.status = 'COMPLETED' AND YEAR(o.order_date) = :year
                        GROUP BY MONTH(o.order_date)
                        ORDER BY MONTH(o.order_date)
                        """)
                        .bind("year", year)
                        .mapToBean(RevenueDTO.class)
                        .list()
        );
    }
    public List<OrderDTOChart> getOrderChart(LocalDate from, LocalDate to) {
        return  getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT DATE(o.order_date) AS label, COUNT(*) AS total
                        FROM ORDERS o
                        INNER JOIN USER u ON u.id = o.user_id
                        WHERE u.role = 0 AND o.status = 'COMPLETED' AND o.order_date BETWEEN :from AND :to
                        GROUP BY DATE(o.order_date)
                        ORDER BY DATE(o.order_date)
                        LIMIT 30
                        """)
                        .bind("from",from)
                        .bind("to", to)
                        .mapToBean(OrderDTOChart.class)
                        .list()
        );
    }
    public List<OrderDTOChart> getOrderChart(String year) {
        return  getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT CONCAT('Tháng ', MONTH(o.order_date)) AS label, COUNT(*) AS total
                        FROM ORDERS o
                        INNER JOIN USER u ON u.id = o.user_id
                        WHERE u.role = 0 AND o.status = 'COMPLETED' AND YEAR(o.order_date) = :year
                        GROUP BY MONTH(o.order_date)
                        ORDER BY MONTH(o.order_date)
                        """)
                        .bind("year", year)
                        .mapToBean(OrderDTOChart.class)
                        .list()
        );
    }

    public int getTotalSoldProducts(LocalDate from, LocalDate to) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                        SELECT SUM(oi.quantity)
                        FROM order_items oi
                        JOIN orders o ON  o.id = oi.order_id
                        WHERE o.status = 'COMPLETED' AND o.order_date BETWEEN :from AND :to
                        """)
                        .bind("from", from)
                        .bind("to", to)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(0));
    }

    public int getTotalSoldProducts(String year) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                        SELECT SUM(oi.quantity)
                        FROM order_items oi
                        JOIN orders o ON o.id = oi.order_id
                        WHERE o.status = 'COMPLETED' AND YEAR(o.order_date) = :year
                        """)
                        .bind("year", year)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(0));
    }

    public int getTotalStock() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                        SELECT SUM(stock)
                        FROM books
                        """)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(0));
    }

    public int getOutOfStockCount() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                        SELECT COUNT(*)
                        FROM books
                        WHERE stock = 0
                        """)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(0));
    }

    public  List<Book> getOutOfStockBooks() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                            SELECT *
                            FROM books
                            WHERE stock = 0
                            """)
                        .mapToBean(Book.class)
                        .list());
    }

    public int getTotalSoldProductsAllTime() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                    SELECT SUM(oi.quantity)
                    FROM order_items oi
                    JOIN orders o ON o.id = oi.order_id
                    WHERE o.status = 'COMPLETED'
                    """)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(0));
    }

    public int getUnsoldBooksCount() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                        SELECT COUNT(*)
                        FROM books 
                        WHERE id NOT IN (
                            SELECT oi.book_id 
                            FROM order_items oi 
                            JOIN orders o ON o.id = oi.order_id 
                            WHERE o.status = 'COMPLETED'
                        )
                        """)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(0));
    }

    public List<Book> getUnsoldBooks() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                        SELECT * FROM books 
                        WHERE id NOT IN (
                            SELECT oi.book_id 
                            FROM order_items oi 
                            JOIN orders o ON o.id = oi.order_id 
                            WHERE o.status = 'COMPLETED'
                        )
                        """)
                        .mapToBean(Book.class)
                        .list());
    }

    public List<String> listYears() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT DISTINCT YEAR(o.order_date) FROM ORDERS o" )
                .mapTo(String.class).list());
    }
    public Map<String, Double> getSoldByCategory(LocalDate from, LocalDate to) {
        int totalProductSold = getTotalSoldProducts(from, to);
        if (totalProductSold == 0) return Collections.emptyMap();
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                            SELECT b.type, (SUM(oi.quantity)*100.0)/:totalProductSold AS total
                            FROM ORDERS o
                            INNER JOIN ORDER_ITEMS oi ON o.id = oi.order_id
                            INNER JOIN BOOKS b ON b.id = oi.book_id
                            WHERE o.status = 'COMPLETED'
                              AND o.order_date BETWEEN :from AND :to
                            GROUP BY b.type
                        """)
                        .bind("totalProductSold", totalProductSold)
                        .bind("from", from)
                        .bind("to", to)
                        .map((rs, ctx) -> Map.entry(
                                rs.getString("type"),
                                rs.getDouble("total")
                        ))
                        .collect(Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry.getValue()
                        ))
        );
    }
    public Map<String, Double> getSoldByCategory(String year) {
        int totalProductSold = getTotalSoldProducts(year);
        if (totalProductSold == 0) return Collections.emptyMap();
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                            SELECT b.type, (SUM(oi.quantity)*100.0)/:totalProductSold AS total
                            FROM ORDERS o
                            INNER JOIN ORDER_ITEMS oi ON o.id = oi.order_id
                            INNER JOIN BOOKS b ON b.id = oi.book_id
                            WHERE o.status = 'COMPLETED'
                              AND YEAR(o.order_date) = :year
                            GROUP BY b.type
                        """)
                        .bind("totalProductSold", totalProductSold)
                        .bind("year", year)
                        .map((rs, ctx) -> Map.entry(
                                rs.getString("type"),
                                rs.getDouble("total")
                        ))
                        .collect(Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry.getValue()
                        ))
        );
    }
    public Map<String, Double> getPercentProfitByCategory(String year) {
        double totalRevenue = getTotalRevenue(year);
        if (totalRevenue == 0) {
            return Collections.emptyMap();
        }
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                    SELECT b.type,
                                        (SUM(oi.subtotal) * 100.0) / :totalRevenue AS total
                                    FROM ORDERS o
                                    INNER JOIN ORDER_ITEMS oi ON o.id = oi.order_id
                                    INNER JOIN BOOKS b ON b.id = oi.book_id
                                    WHERE o.status = 'COMPLETED' AND YEAR(o.order_date) = :year
                                    GROUP BY b.type
                                """)
                        .bind("totalRevenue", totalRevenue)
                        .bind("year", year)
                        .map((rs, ctx) -> Map.entry(
                                rs.getString("type"),
                                rs.getDouble("total")
                        ))
                        .collect(Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry.getValue()
                        ))
        );
    }
    public Map<String, Double> getPercentProfitByCategory(LocalDate from, LocalDate to) {
        double totalRevenue = getTotalRevenue(from, to);
        if (totalRevenue == 0) {
            return Collections.emptyMap();
        }
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                    SELECT b.type,
                                        (SUM(oi.subtotal) * 100.0) / :totalRevenue AS total
                                    FROM ORDERS o
                                    INNER JOIN ORDER_ITEMS oi ON o.id = oi.order_id
                                    INNER JOIN BOOKS b ON b.id = oi.book_id
                                    WHERE o.status = 'COMPLETED' AND YEAR(o.order_date) = :year
                                    GROUP BY b.type
                                """)
                        .bind("totalRevenue", totalRevenue)
                        .bind("from", from)
                        .bind("to", to)
                        .map((rs, ctx) -> Map.entry(
                                rs.getString("type"),
                                rs.getDouble("total")
                        ))
                        .collect(Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry.getValue()
                        ))
        );
    }
    public int getTotalOrders(LocalDate from, LocalDate to) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                        SELECT COUNT(*)
                        FROM orders o
                        INNER JOIN USER u ON u.id = o.user_id
                        WHERE u.role = 0 AND o.status = 'COMPLETED' AND o.order_date BETWEEN :from AND :to
                        """)
                        .bind("from", from)
                        .bind("to", to)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(0));
    }
    public int getTotalOrders(String year) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                        SELECT COUNT(*)
                        FROM orders o
                        INNER JOIN USER u ON u.id = o.user_id
                        WHERE u.role = 0 AND o.status = 'COMPLETED' AND YEAR(o.order_date)=:year
                        """)
                        .bind("year", year)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(0));
    }
    public int getTotalCancelledOrders(String year) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                        SELECT COUNT(*)
                        FROM orders
                        WHERE status = 'CANCELLED' AND YEAR(order_date)=:year
                        """)
                        .bind("year", year)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(0));
    }
    public int getTotalCancelledOrders(LocalDate from, LocalDate to) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                        SELECT COUNT(*)
                        FROM orders
                        WHERE status = 'CANCELLED' AND order_date BETWEEN :from AND :to
                        """)
                        .bind("from", from)
                        .bind("to", to)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(0));
    }

    public static void main(String[] args) {
        ThongKeDao thongkeDao = new ThongKeDao();
        LocalDate now = LocalDate.now();
    }
}
