package dao;

import model.Notification;

import java.util.List;

public class NotificationDAO extends BaseDao {

    public void insert(Notification n) {
        getJdbi().useHandle(handle -> {
            handle.createUpdate(
                            "INSERT INTO notification (user_id, title, noti, is_read) " +
                                    "VALUES (:userId, :title, :noti, 0)"
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
                    is_read       AS `read`, 
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

    public int countByUser(int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT COUNT(*)
                        FROM notification
                        WHERE user_id = :uid
                        """)
                        .bind("uid", userId)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public int countUnread(int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT COUNT(*)
                        FROM notification
                        WHERE user_id = :userId
                        AND is_read=0
                        """)
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public void markReadById(int id){
        getJdbi().useHandle(handle ->
                handle.createUpdate("""
                        UPDATE notification
                        SET is_read = 1
                        WHERE id = :id
                        """)
                        .bind("id", id)
                        .execute()
        );
    }

    public void markAsRead(int userId){
        getJdbi().useHandle(handle ->
                handle.createUpdate("""
                        UPDATE notification
                        SET is_read=1
                        WHERE user_id= :userId
                        """)
                        .bind("userId", userId)
                        .execute()
        );
    }
}
