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
}