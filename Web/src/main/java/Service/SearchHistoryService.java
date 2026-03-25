package Service;

import dao.SearchHistoryDAO;

import java.util.List;

public class SearchHistoryService {
    SearchHistoryDAO dao = new SearchHistoryDAO();

    public void save(int userId, String keyword) {
        if(keyword == null || keyword.isBlank()) return;
        dao.save(userId, keyword.trim());
    }

    public List<String> getHistory(int userId) {
        return dao.getRecent(userId);
    }
}
