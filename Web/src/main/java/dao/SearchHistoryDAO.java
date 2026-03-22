package dao;

import java.util.List;

public class SearchHistoryDAO extends BaseDao {

    public void save(int userId, String keyword) {
        getJdbi().useHandle(h ->
                h.createUpdate("""
                        INSERT INTO search_history(user_id, keyword)
                        VALUES(:uid,:k)
                        """)
                        .bind("uid", userId)
                        .bind("k", keyword)
                        .execute()
        );
    }

    public List<String> getRecent(int userId) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                        SELECT keyword
                        FROM search_history
                        WHERE user_id=:uid
                        GROUP BY keyword
                        ORDER BY MAX(created_at) DESC
                        LIMIT 8
                """)
                .bind("uid", userId)
                .mapTo(String.class)
                .list()
        );
    }
}
