package dao;

import model.AdminBookRateView;
import model.CommentAdmin;
import model.CommentView;
import model.RatingStartView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommentDao extends BaseDao{
    public List<CommentView> getAllComment(int bookId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT  u.name AS name , c.rating AS rating, c.content  AS content, DATE_FORMAT(c.create_at, '%d/%m/%Y') AS createAt, c.img_comment AS imgComment" +
                                        " FROM comments c" +
                                        " INNER JOIN USER u ON u.id = c.user_id" +
                                        " WHERE c.book_id = :book_id  AND c.is_active=1 ORDER BY c.create_at DESC")
                        .bind("book_id", bookId)
                        .mapToBean(CommentView.class)
                        .list()
        );
    }
    public CommentView getCommentByOrder(int bookId, int orderId, int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                    SELECT  u.name AS name , c.rating AS rating, c.content  AS content, DATE_FORMAT(c.create_at, '%d/%m/%Y') AS createAt, c.img_comment AS imgComment
                                    FROM comments c
                                    INNER JOIN USER u ON u.id = c.user_id
                                    WHERE c.book_id =:book_id  AND c.order_id=:order_id AND u.id=:user_id  AND c.is_active=1
                                    ORDER BY c.create_at DESC
                                    """)
                        .bind("book_id", bookId)
                        .bind("order_id", orderId)
                        .bind("user_id", userId)
                        .mapToBean(CommentView.class)
                        .findOne()
                        .orElse(null)
        );
    }
    public void insertComment(int userId, int bookId, int orderId, int rating, String content, String imgURL) {
        getJdbi().useHandle(handle ->
                handle.createUpdate(
                                "INSERT INTO comments(user_id, book_id, order_id, rating, content, create_at, img_comment, is_active) " +
                                        "VALUES (:userId, :bookId, :orderId, :rating, :content, NOW(), :imgURL , 1)"
                        )
                        .bind("userId", userId)
                        .bind("bookId", bookId)
                        .bind("orderId", orderId)
                        .bind("rating", rating)
                        .bind("content", content)
                        .bind("imgURL", imgURL)
                        .execute()
        );
    }
    public Set<Integer> getReviewedBookIds(int userId, int orderId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                    SELECT book_id
                                    FROM comments
                                    WHERE user_id = :userId
                                      AND order_id = :orderId
                                    """)
                        .bind("userId", userId)
                        .bind("orderId", orderId)
                        .mapTo(Integer.class)
                        .collect(Collectors.toSet())
        );
    }
    public Double getAverageRating(int bookId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT ROUND(AVG(rating),1) FROM comments WHERE book_id=:book_id")
                        .bind("book_id", bookId)
                        .mapTo(double.class)
                        .findOne()
                        .orElse(0.0)
        );
    }
    public List<RatingStartView> getRatingStartView(int bookId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT rating, COUNT(*) AS total, " +
                                        "ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER (), 2) AS percent " +
                                        "FROM comments " +
                                        "WHERE book_id = :book_id " +
                                        "GROUP BY rating " +
                                        "ORDER BY rating DESC"
                        )
                        .bind("book_id",bookId)
                        .mapToBean(RatingStartView.class)
                        .list()
        );
    }
    public List<CommentView> getCommentByRating(int bookId, int rating) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT  u.name AS name , c.rating AS rating, c.content  AS content, DATE_FORMAT(c.create_at, '%d/%m/%Y') AS createAt, c.img_comment AS imgComment" +
                                        " FROM comments c" +
                                        " INNER JOIN USER u ON u.id = c.user_id" +
                                        " WHERE c.book_id = :book_id AND c.rating = :rating AND c.is_active=1 ORDER BY c.create_at DESC")
                        .bind("book_id", bookId)
                        .bind("rating", rating)
                        .mapToBean(CommentView.class)
                        .list()
        );
    }
    public int countByStar(int star, LocalDate from, LocalDate to, String type) {
        return getJdbi().withHandle(handle -> {
                return handle.createQuery("""
                                        SELECT COUNT(*)
                                        FROM comments c
                                        INNER JOIN books b ON b.id = c.book_id
                                        WHERE c.rating = :star
                                                AND c.create_at >= :from
                                                AND c.create_at <= :to
                                                AND b.type = :type
                                    """)
                    .bind("star", star)
                    .bind("from", from)
                    .bind("to", to)
                    .bind("type", type)
                    .mapTo(int.class)
                    .one();
        });
    }
    public int countByStar(int star, LocalDate from, LocalDate to) {
        return getJdbi().withHandle(handle -> {
            return handle.createQuery("""
                                        SELECT COUNT(*)
                                        FROM comments
                                        WHERE rating = :star
                                                AND create_at >= :from
                                                AND create_at <= :to
                                    """)
                    .bind("star", star)
                    .bind("from", from)
                    .bind("to", to)
                    .mapTo(int.class)
                    .one();
        });
    }
    public int countByStar(int star, String type) {
        return getJdbi().withHandle(handle -> {
            return handle.createQuery("""
                                        SELECT COUNT(*)
                                        FROM comments c
                                        INNER JOIN books b ON b.id = c.book_id
                                        WHERE c.rating = :star
                                              AND b.type = :type
                                    """)
                    .bind("star", star)
                    .bind("type", type)
                    .mapTo(int.class)
                    .one();
        });
    }
    public int countByStar(int star) {
        return getJdbi().withHandle(handle -> {
            return handle.createQuery("""
                                        SELECT COUNT(*)
                                        FROM comments c
                                        INNER JOIN books b ON b.id = c.book_id
                                        WHERE c.rating = :star
                                    """)
                    .bind("star", star)
                    .mapTo(int.class)
                    .one();
        });
    }

    public List<AdminBookRateView> getAdminBookRateHigh(LocalDate from, LocalDate to, String type) {
        return getJdbi().withHandle(handle -> {
             return   handle.createQuery("""
                                        SELECT b.title, AVG(c.rating) AS rating
                                        FROM comments c
                                        INNER JOIN books b ON b.id = c.book_id
                                        WHERE b.type = :type
                                                 AND c.rating BETWEEN 4 AND 5
                                                 AND c.create_at >= :from
                                                 AND c.create_at <= :to
                                        GROUP BY b.id, b.title
                                        ORDER BY rating DESC
                                    """)
                        .bind("type", type)
                        .bind("from", from)
                        .bind("to", to)
                        .mapToBean(AdminBookRateView.class)
                        .list();
        });
    }
    public List<AdminBookRateView> getAdminBookRateHigh(LocalDate from, LocalDate to) {
        return getJdbi().withHandle(handle -> {
            return   handle.createQuery("""
                                        SELECT b.title, AVG(c.rating) AS rating
                                        FROM comments c
                                        INNER JOIN books b ON b.id = c.book_id
                                        WHERE rating BETWEEN 4 AND 5
                                            AND c.create_at >= :from
                                            AND c.create_at <= :to
                                        GROUP BY b.id, b.title
                                        ORDER BY rating DESC
                                    """)
                    .bind("from", from)
                    .bind("to", to)
                    .mapToBean(AdminBookRateView.class)
                    .list();
        });
    }
    public List<AdminBookRateView> getAdminBookRateHigh() {
        return getJdbi().withHandle(handle -> {
            return   handle.createQuery("""
                                        SELECT b.title, AVG(c.rating) AS rating
                                        FROM comments c
                                        INNER JOIN books b ON b.id = c.book_id
                                        WHERE rating BETWEEN 4 AND 5
                                        GROUP BY b.id, b.title
                                        ORDER BY rating DESC
                                    """)
                    .mapToBean(AdminBookRateView.class)
                    .list();
        });
    }
    public List<AdminBookRateView> getAdminBookRateHigh(String type) {
        return getJdbi().withHandle(handle -> {
            return   handle.createQuery("""
                                        SELECT b.title, AVG(c.rating) AS rating
                                        FROM comments c
                                        INNER JOIN books b ON b.id = c.book_id
                                        WHERE b.type = :type
                                            AND rating BETWEEN 4 AND 5
                                        GROUP BY b.id, b.title
                                        ORDER BY rating DESC
                                    """)
                    .bind("type", type)
                    .mapToBean(AdminBookRateView.class)
                    .list();
        });
    }

    public List<AdminBookRateView> getAdminBookRateLow(LocalDate from, LocalDate to) {
        return getJdbi().withHandle(handle -> {
            return   handle.createQuery("""
                                        SELECT b.title, AVG(c.rating) AS rating
                                        FROM comments c
                                        INNER JOIN books b ON b.id = c.book_id
                                        WHERE c.create_at >= :from
                                            AND c.create_at <= :to
                                            AND rating BETWEEN 1 AND 3
                                        GROUP BY b.id, b.title
                                        ORDER BY rating ASC
                                    """)
                    .bind("from", from)
                    .bind("to", to)
                    .mapToBean(AdminBookRateView.class)
                    .list();
        });
    }
    public List<AdminBookRateView> getAdminBookRateLow(LocalDate from, LocalDate to,String type) {
        return getJdbi().withHandle(handle -> {
            return   handle.createQuery("""
                                        SELECT b.title, AVG(c.rating) AS rating
                                        FROM comments c
                                        INNER JOIN books b ON b.id = c.book_id
                                        WHERE b.type = :type
                                            AND rating BETWEEN 1 AND 3
                                            AND c.create_at >= :from
                                            AND c.create_at <= :to
                                        GROUP BY b.id, b.title
                                        ORDER BY rating ASC,c.create_at DESC
                                    """)
                    .bind("from", from)
                    .bind("to", to)
                    .bind("type", type)
                    .mapToBean(AdminBookRateView.class)
                    .list();
        });
    }
    public List<AdminBookRateView> getAdminBookRateLow(String type) {
        return getJdbi().withHandle(handle -> {
            return   handle.createQuery("""
                                        SELECT b.title, AVG(c.rating) AS rating
                                        FROM comments c
                                        INNER JOIN books b ON b.id = c.book_id
                                        WHERE b.type = :type
                                            AND rating BETWEEN 1 AND 3
                                        GROUP BY b.id, b.title
                                        ORDER BY rating ASC,c.create_at DESC

                                    """)
                    .bind("type", type)
                    .mapToBean(AdminBookRateView.class)
                    .list();
        });
    }
    public List<AdminBookRateView> getAdminBookRateLow() {
        return getJdbi().withHandle(handle -> {
            return   handle.createQuery("""
                                        SELECT b.title, AVG(c.rating) AS rating
                                        FROM comments c
                                        INNER JOIN books b ON b.id = c.book_id
                                        WHERE rating BETWEEN 1 AND 3
                                        GROUP BY b.id, b.title
                                        ORDER BY rating ASC,c.create_at DESC
                                    """)
                    .mapToBean(AdminBookRateView.class)
                    .list();
        });
    }

    public List<CommentAdmin> getCommentAdmin() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                        SELECT c.id,b.title AS title,u.name AS name,c.rating,c.content,c.create_at AS createAt,c.is_active AS isActive
                                        FROM comments c
                                        JOIN books b ON c.book_id = b.id
                                        JOIN user u ON c.user_id = u.id
                                        ORDER BY c.create_at DESC
                                        """)
                        .mapToBean(CommentAdmin.class)
                        .list()
        );
    }
    public List<CommentAdmin> getCommentAdmin(LocalDate from, LocalDate to) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                        SELECT c.id,b.title AS title,u.name AS name,c.rating,c.content,c.create_at AS createAt,c.is_active AS isActive
                                        FROM comments c
                                        JOIN books b ON c.book_id = b.id
                                        JOIN user u ON c.user_id = u.id
                                        WHERE  c.create_at >= :from AND c.create_at <= :to
                                        ORDER BY c.create_at DESC
                                        """)
                        .bind("from", from)
                        .bind("to", to)
                        .mapToBean(CommentAdmin.class)
                        .list()
        );
    }
    public List<CommentAdmin> getCommentAdmin(LocalDate from, LocalDate to, String type) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                        SELECT c.id,b.title AS title,u.name AS name,c.rating,c.content,c.create_at AS createAt,c.is_active AS isActive
                                        FROM comments c
                                        JOIN books b ON c.book_id = b.id
                                        JOIN user u ON c.user_id = u.id
                                        WHERE  c.create_at >= :from AND c.create_at <= :to AND b.type = :type
                                        ORDER BY c.create_at DESC
                                        """)
                        .bind("from", from)
                        .bind("to", to)
                        .bind("type", type)
                        .mapToBean(CommentAdmin.class)
                        .list()
        );
    }
    public List<CommentAdmin> getCommentAdmin(String type) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                        SELECT c.id,b.title AS title,u.name AS name,c.rating,c.content,c.create_at AS createAt,c.is_active AS isActive
                                        FROM comments c
                                        JOIN books b ON c.book_id = b.id
                                        JOIN user u ON c.user_id = u.id
                                        WHERE b.type = :type
                                        ORDER BY c.create_at DESC
                                        """)
                        .bind("type", type)
                        .mapToBean(CommentAdmin.class)
                        .list()
        );
    }

    public void setActive(int id){
        getJdbi().withHandle(handle ->
                handle.createUpdate("""
                                    UPDATE comments
                                    SET is_active = CASE
                                        WHEN is_active = 1 THEN 0
                                        ELSE 1
                                    END
                                    WHERE id = :id
                                """)
                        .bind("id", id)
                        .execute()
        );
    }
    public void deleteRate(int id){
        getJdbi().withHandle(handle ->
                handle.createUpdate("DELETE FROM comments c WHERE c.id = :id")
                        .bind("id", id)
                        .execute()
        );
    }

    public static void main(String[] args) {
        CommentDao dao = new CommentDao();
        List<RatingStartView> comments = dao.getRatingStartView(2);
        LocalDate from =  LocalDate.of(2018, 1, 1);
        LocalDate to =  LocalDate.now();
    }

}
