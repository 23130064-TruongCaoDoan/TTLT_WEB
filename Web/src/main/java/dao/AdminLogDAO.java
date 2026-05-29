package dao;

import model.HistoryLog;

import java.util.List;

public class AdminLogDAO extends BaseDao {
    public void insertLog(int userId, String actionType, String actionUrl) {
        getJdbi().useHandle(h -> {
            h.createUpdate("INSERT INTO account_history (user_id, action_type, action_url) " +
                            "VALUES (:userId, :actionType, :actionUrl)")
                    .bind("userId", userId)
                    .bind("actionType", actionType)
                    .bind("actionUrl", actionUrl)
                    .execute();
        });
    }

    public List<HistoryLog> getHistoryByUserId(int userId) {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT * FROM account_history WHERE user_id = :userId ORDER BY created_at DESC LIMIT 50")
                        .bind("userId", userId)
                        .mapToBean(HistoryLog.class)
                        .list()
        );
    }

    public int getUnreadCount(int userId) {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT COUNT(*) FROM account_history WHERE user_id = :userId AND is_read = 0")
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public void markAllAsRead(int userId) {
        getJdbi().useHandle(h -> {
            h.createUpdate("UPDATE account_history SET is_read = 1 WHERE user_id = :userId AND is_read = 0")
                    .bind("userId", userId)
                    .execute();
        });
    }
}