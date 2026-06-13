package dao;

import model.Publisher;
import java.util.List;

public class PublisherDao extends BaseDao {

    public List<Publisher> searchAndPaginate(String query, int limit, int offset) {
        return getJdbi().withHandle(handle -> {
            StringBuilder sql = new StringBuilder("SELECT * FROM Publisher WHERE is_deleted = 0 ");
            if (query != null && !query.trim().isEmpty()) {
                sql.append("AND (publisherCode LIKE :q OR name LIKE :q) ");
            }
            sql.append("ORDER BY id DESC LIMIT :limit OFFSET :offset");

            var q = handle.createQuery(sql.toString());
            if (query != null && !query.trim().isEmpty()) {
                q.bind("q", "%" + query.trim() + "%");
            }
            return q.bind("limit", limit)
                    .bind("offset", offset)
                    .mapToBean(Publisher.class)
                    .list();
        });
    }

    public int countSearch(String query) {
        return getJdbi().withHandle(handle -> {
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Publisher WHERE is_deleted = 0 ");
            if (query != null && !query.trim().isEmpty()) {
                sql.append("AND (publisherCode LIKE :q OR name LIKE :q) ");
            }
            var q = handle.createQuery(sql.toString());
            if (query != null && !query.trim().isEmpty()) {
                q.bind("q", "%" + query.trim() + "%");
            }
            return q.mapTo(Integer.class).one();
        });
    }

    public boolean isNameExists(String name) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM Publisher WHERE name = :name AND is_deleted = 0")
                        .bind("name", name)
                        .mapTo(Integer.class).one() > 0
        );
    }

    public Publisher getById(int id) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM Publisher WHERE id = :id AND is_deleted = 0")
                        .bind("id", id)
                        .mapToBean(Publisher.class).findOne().orElse(null)
        );
    }

    public boolean insertPublisher(Publisher p) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("INSERT INTO Publisher (publisherCode, name, address, email, phone) VALUES (:code, :name, :address, :email, :phone)")
                        .bind("code", p.getPublisherCode())
                        .bind("name", p.getName())
                        .bind("address", p.getAddress())
                        .bind("email", p.getEmail())
                        .bind("phone", p.getPhone())
                        .execute() > 0
        );
    }

    public boolean updatePublisher(Publisher p) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE Publisher SET name = :name, address = :address, email = :email, phone = :phone WHERE id = :id")
                        .bind("name", p.getName())
                        .bind("address", p.getAddress())
                        .bind("email", p.getEmail())
                        .bind("phone", p.getPhone())
                        .bind("id", p.getId())
                        .execute() > 0
        );
    }

    public boolean deletePublisher(int id) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE Publisher SET is_deleted = 1 WHERE id = :id")
                        .bind("id", id)
                        .execute() > 0
        );
    }
}