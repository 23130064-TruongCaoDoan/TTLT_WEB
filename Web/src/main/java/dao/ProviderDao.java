package dao;

import model.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderDao extends BaseDao {

    public List<Provider> getProvidersPaginated(String q, int limit, int offset) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, COUNT(b.id) AS bookCount FROM providers p " +
                        "LEFT JOIN books b ON b.provider = p.name AND b.is_sell = 1 " +
                        "WHERE p.is_deleted = 0 "
        );

        if (q != null && !q.trim().isEmpty()) {
            sql.append("AND (p.code LIKE :q OR p.name LIKE :q) ");
        }
        sql.append("GROUP BY p.id LIMIT :limit OFFSET :offset");

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            if (q != null && !q.trim().isEmpty()) {
                query.bind("q", "%" + q.trim() + "%");
            }
            return query.bind("limit", limit)
                    .bind("offset", offset)
                    .mapToBean(Provider.class)
                    .list();
        });
    }

    public int countTotal(String q) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM providers WHERE is_deleted = 0 ");
        if (q != null && !q.trim().isEmpty()) {
            sql.append("AND (code LIKE :q OR name LIKE :q) ");
        }
        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            if (q != null && !q.trim().isEmpty()) {
                query.bind("q", "%" + q.trim() + "%");
            }
            return query.mapTo(Integer.class).one();
        });
    }

    public boolean existsByCode(String code) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM providers WHERE code = :code AND is_deleted = 0")
                        .bind("code", code)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public boolean insert(Provider provider) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("INSERT INTO providers (code, name, address, email, phone, is_deleted) " +
                                "VALUES (:code, :name, :address, :email, :phone, 0)")
                        .bindBean(provider)
                        .execute() > 0
        );
    }

    public boolean updateSelective(Provider provider) {
        StringBuilder sql = new StringBuilder("UPDATE providers SET ");
        Map<String, Object> binds = new HashMap<>();

        if (provider.getName() != null) {
            sql.append("name = :name, ");
            binds.put("name", provider.getName());
        }
        if (provider.getAddress() != null) {
            sql.append("address = :address, ");
            binds.put("address", provider.getAddress());
        }
        if (provider.getEmail() != null) {
            sql.append("email = :email, ");
            binds.put("email", provider.getEmail());
        }
        if (provider.getPhone() != null) {
            sql.append("phone = :phone, ");
            binds.put("phone", provider.getPhone());
        }

        int lastComma = sql.lastIndexOf(", ");
        if (lastComma == -1) return false;
        sql.delete(lastComma, sql.length());

        sql.append(" WHERE id = :id AND is_deleted = 0");
        binds.put("id", provider.getId());

        return getJdbi().withHandle(handle -> {
            var update = handle.createUpdate(sql.toString());
            binds.forEach(update::bind);
            return update.execute() > 0;
        });
    }

    public boolean deleteSoft(int id) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE providers SET is_deleted = 1 WHERE id = :id")
                        .bind("id", id)
                        .execute() > 0
        );
    }

    public Provider getById(int id) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM providers WHERE id = :id AND is_deleted = 0")
                        .bind("id", id)
                        .mapToBean(Provider.class).findOne().orElse(null)
        );
    }
}