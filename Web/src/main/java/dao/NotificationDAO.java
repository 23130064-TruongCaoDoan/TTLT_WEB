package dao;

import model.Notification;

import java.util.List;

public class NotificationDAO extends BaseDao {

    public void insert(Notification n) {
        getJdbi().useHandle(handle -> {
            handle.createUpdate(
                            "INSERT INTO notification (user_id, title, noti) " +
                                    "VALUES (:userId, :title, :noti)"
                    )
                    .bind("userId", n.getUserId())
                    .bind("title", n.getTitle())
                    .bind("noti", n.getNoti())
                    .execute();
        });
    }

    public List<Notification> findByUser(int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                SELECT
                    id            AS notiId,
                    user_id       AS userId,
                    title,
                    noti,
                    DATE_FORMAT(create_at, '%H:%i %d/%m/%Y') AS createdAt
                FROM notification
                WHERE user_id = :uid
                ORDER BY create_at DESC
            """)
                        .bind("uid", userId)
                        .mapToBean(Notification.class)
                        .list()
        );
    }
}
